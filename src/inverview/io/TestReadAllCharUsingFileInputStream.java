package inverview.io;

import java.io.FileInputStream;

public class TestReadAllCharUsingFileInputStream {
	public static void main(String args[]) {
		try (FileInputStream fileInputStream = new FileInputStream(
				"/Users/liuyingjie/eclipse-workspace/learning/src/inverview/io/notes")) {
			int num = 0;
			while ((num = fileInputStream.read()) != -1) {
				System.out.print((char) num);
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}
	}

}
