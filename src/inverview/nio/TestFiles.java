package inverview.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class TestFiles {

	public static void main(String[] args) throws IOException {

		/* Test Exists */
		Path path = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/notexists");
		boolean pathExists = Files.exists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
		System.out.println("pathExists: " + pathExists);

		/* java.nio.file.FileAlreadyExistsException when run the second time */
		Path pathToCreate = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/tocreate");
		if (Files.exists(pathToCreate)) {
			Files.delete(pathToCreate);
		}
		Path newDir = Files.createDirectory(pathToCreate);
		System.out.println("newDir: " + newDir);

		/* Test copy */
		Path sourcePath = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/myfile.txt");
		Path destinationPath = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/copiedmyfiles.txt");
		if (Files.exists(destinationPath)) {
			Files.delete(destinationPath);
		}
		Files.copy(sourcePath, destinationPath);
		System.out.println("destinationPath: " + destinationPath);

		/* Test override */
		Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

		Path movedPath = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/movedmyfiles.txt");
		Files.move(destinationPath, movedPath, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("movedPath: " + movedPath);

		/* Test walk tree */
		Path pathToWalk = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio");
		Files.walkFileTree(pathToWalk, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				System.out.println("pre visit dir:" + dir);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				System.out.println("visit file: " + file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				System.out.println("visit file failed: " + file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				System.out.println("post visit directory: " + dir);
				return FileVisitResult.CONTINUE;
			}
		});

		/* Search for file */
		String fileToFind = File.separator + "movedmyfiles.txt";
		Files.walkFileTree(pathToWalk, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String fileString = file.toAbsolutePath().toString();
				if (fileString.endsWith(fileToFind)) {
					System.out.println("file found at path: " + file.toAbsolutePath());
					return FileVisitResult.TERMINATE;
				}
				return FileVisitResult.CONTINUE;
			}
		});

	}

}
