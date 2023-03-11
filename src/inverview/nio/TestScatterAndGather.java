package inverview.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestScatterAndGather {

	public static void main(String[] args) throws IOException {

		RandomAccessFile aFile = new RandomAccessFile(
				"/Users/liuyingjie/git/leetcode/src/inverview/nio/data/nio-data.txt", "rw");
		FileChannel inChannel = aFile.getChannel();

		ByteBuffer header = ByteBuffer.allocate(128);
		ByteBuffer body = ByteBuffer.allocate(1024);
		ByteBuffer[] bufferArray = { header, body };
		inChannel.read(bufferArray);

		System.out.println("Read header");
		header.flip();

		while (header.hasRemaining()) {
			System.out.print((char) header.get());
		}
		header.clear();
		
		System.out.println("");
		System.out.println("Read Body");
		body.flip();

		while (body.hasRemaining()) {
			System.out.print((char) body.get());
		}
		body.clear();

		aFile.close();

	}

}
