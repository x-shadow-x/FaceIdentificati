package pretreatment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 将图片转为libsvm规定的格式的数据~存到项目路径下~~OpenCVtrain.txt
 * @author Administrator
 *
 */
public class TrainData {

	public static void main(String[] args) throws FileNotFoundException, IOException{

		FileWriter fw1 = new FileWriter("OpenCVtrain.txt");	

		
		File[] subFiles = new File("train").listFiles();
		for(int i = 0; i < subFiles.length; i++){
			File[] tempFiles = subFiles[i].listFiles();
			for(int j = 0; j < tempFiles.length; j++){
				System.out.println(tempFiles[j].getPath());
				BufferedImage img = ImageIO.read(new FileInputStream(tempFiles[j].getPath()));
				int[] temp = new int[10000];
				int index = 0;
				for(int ii = 0; ii < 100; ii++){
					for(int jj = 0; jj < 100; jj++){
						temp[index++] = img.getRGB(ii, jj);
					}
				}
				StringBuilder sbBuilder = new StringBuilder();
				sbBuilder.append(subFiles[i].getName());
				for(int k = 0; k < 10000; k++){
					sbBuilder.append(" " + k + ":" + temp[k]);
				}
				sbBuilder.append("\r\n");
				fw1.write(sbBuilder.toString());
				
			}
			
		}
		fw1.close();
	}
}
