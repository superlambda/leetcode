package inverview.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class TestChannelTransfer {

	public static void main(String[] args) throws IOException {
		try (RandomAccessFile fromFile = new RandomAccessFile(
				"/Users/liuyingjie/git/leetcode/src/inverview/nio/data/nio-data.txt", "rw");
				RandomAccessFile toFile = new RandomAccessFile(
						"/Users/liuyingjie/git/leetcode/src/inverview/nio/data/nio-data-transfered.txt", "rw");) {
			FileChannel fromChannel = fromFile.getChannel();
			FileChannel toChannel = toFile.getChannel();

			long position = 0;
			long count = fromChannel.size();

			toChannel.transferFrom(fromChannel, position, count);
			toChannel.close();
		}
	}

}
