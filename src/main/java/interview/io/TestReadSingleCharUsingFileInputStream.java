package interview.io;

import java.io.FileInputStream;

public class TestReadSingleCharUsingFileInputStream {

	public static void main(String args[]) {
		try (FileInputStream fileInputStream = new FileInputStream("/Users/liuyingjie/eclipse-workspace/learning/src/interview/io/notes")) {
			int num = fileInputStream.read();
			System.out.print((char) num);
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

}
