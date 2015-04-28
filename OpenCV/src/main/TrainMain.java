package main;

import java.io.IOException;
import java.sql.Date;

import svm.*;

public class TrainMain {

	public static void train() throws IOException {
		String[] arg = { "-t", "0", "OpenCVTrain.txt", "OpenCVmodle.txt" };
		svm_train.main(arg);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("==========SVM开始运行============");
		train();
	}
}
