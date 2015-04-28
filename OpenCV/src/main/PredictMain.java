package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;
import svm.svm_predict;

import com.atul.JavaOpenCV.Imshow;

/**
 * 通过摄像头获得人脸图像并将其所方程100*100的图片保存起来
 * 
 * @author Administrator
 *
 */
public class PredictMain {
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	Imshow imshow = null;
	String xmlfilePath = null;
	ContinuousAudioDataStream audioInStream = null;
	CascadeClassifier faceDetector = null;

	public PredictMain() {
		init();
	}

	private void init() {
		imshow = new Imshow("test");
		xmlfilePath = "cascade/haarcascade_frontalface_alt.xml";
		faceDetector = new CascadeClassifier(xmlfilePath);
		svm_predict.loadModel("OpenCVmodle.txt");
		try {
			AudioStream as = new AudioStream(new FileInputStream(new File("a.wav")));
			AudioData data = as.getData();
			audioInStream = new ContinuousAudioDataStream (data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * step1:
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {

		VideoCapture camera = new VideoCapture(0);
		camera.open(0);

		if (!camera.isOpened()) {
			System.out.println("Camera Error");
		} else {
			System.out.println("Camera OK?");
		}

		Mat frame = new Mat();

		ExecutorService pool = Executors.newFixedThreadPool(40);
		// PredictThread t1 = new PredictThread(frame);
		// 运行程序后会调用摄像头一直在获得人脸并实时将其保存到本地
		while (true) {
			camera.read(frame);

			Mat cropped = null;
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(frame, faceDetections);
			for (Rect rect : faceDetections.toArray()) {
				Core.rectangle(frame, new Point(rect.x, rect.y), new Point(
						rect.x + rect.width, rect.y + rect.height), new Scalar(
						0, 255, 0));

				Rect roi = new Rect(rect.x, rect.y, rect.width, rect.height);
				cropped = new Mat(frame, roi);
				// imshow.showImage(cropped);
			}
			if (cropped != null) {// 显示摄像头捕捉到的图像并转为一维整型数组
				imshow.showImage(cropped);
				Mat resizeimage = new Mat();
				Size sz = new Size(100, 100);
				Imgproc.resize(cropped, resizeimage, sz);

				MatOfByte bytemat = new MatOfByte();

				Highgui.imencode(".png", resizeimage, bytemat);

				byte[] bytes = bytemat.toArray();

				InputStream in = new ByteArrayInputStream(bytes);

				//将处理好的数据传到线程中进行预测
				BufferedImage img = ImageIO.read(in);
				PredictThread thread = new PredictThread(img);
				pool.execute(thread);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class PredictThread implements Runnable {

		BufferedImage img;

		public PredictThread(BufferedImage img) {

			this.img = img;

		}

		@Override
		public void run() {
			try {

				int[] temp = new int[img.getWidth() * img.getHeight()];
				int index = 0;
				for (int i = 0; i < img.getHeight(); i++) {
					for (int j = 0; j < img.getWidth(); j++) {
						temp[index++] = img.getRGB(i, j);
					}
				}

				StringBuilder sbBuilder = new StringBuilder();
				sbBuilder.append("1");
				for (int k = 0; k < 10000; k++) {
					sbBuilder.append(" " + k + ":" + temp[k]);
				}
				sbBuilder.append("\r\n");
				FileWriter fw1 = new FileWriter("OpenCVtest.txt");
				fw1.write(sbBuilder.toString());
				fw1.close();
				predict();
				readResult();
				

			} catch (Exception e) {
				
			}
		}
		
	}
	
	private synchronized void readResult() throws IOException{
		System.out.println("hahahahahhahahahha");
		InputStreamReader isReader = new InputStreamReader(new FileInputStream("OpenCVresult.txt"));
		BufferedReader reader = new BufferedReader(isReader);
		String aline = "";
		while((aline = reader.readLine()) != null){
			System.out.println("===========================" + aline);
			if(aline.equals("1.0")){
				AudioPlayer.player.start(new FileInputStream(new File("a.wav")));
			}/*else{
				AudioPlayer.player.stop(audioInStream);
			}*/
		}
		reader.close();
		isReader.close();
	}
	
	
	public static void predict() throws IOException{
		System.out.println("begin");
		String[] parg = {"OpenCVtest.txt","OpenCVmodle.txt","OpenCVresult.txt"};
		//String[] parg = {"OpenCVtest.txt","OpenCVmodle.txt"};
		svm_predict.main(parg);
	}
	
	

	public static void main(String[] args) throws IOException {
		System.out.println("Hello OpenCV");
		System.loadLibrary("opencv_java2411");
		new PredictMain().run();
	}

}
