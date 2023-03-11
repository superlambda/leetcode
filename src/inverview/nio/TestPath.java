package inverview.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPath {

	public static void main(String[] args) throws IOException {
		 Path path = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/myfile.txt");
		 Path path2 = Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/myfile.txt");
		 System.out.println(path);
		 System.out.println("path and path2 are the same path: " + Files.isSameFile(path, path2));
		 
		 Path currentDir = Paths.get(".");
		 System.out.println(" Relative path: "+currentDir+ " absolute path: "+currentDir.toAbsolutePath());
		 
		 Path parentDir = Paths.get("..");
		 System.out.println(parentDir.toAbsolutePath());
		 
		 
		 Path basePath = Paths.get("/Users");

		 Path basePathToPath = basePath.relativize(path);
		 Path pathToBasePath = path.relativize(basePath);

		 System.out.println("Base path to path: " + basePathToPath);
		 System.out.println("Path to base path: " + pathToBasePath);
		 
		 Path basePathToCheck=Paths.get("/Users/liuyingjie/git/leetcode/src/inverview/nio/myfile.txt" +"/" + pathToBasePath);
		 System.out.println("normallize basePathToCheck: " + basePathToCheck.normalize());
		 System.out.println("basePathToPath and basePath are the same path: " + Files.isSameFile(basePathToCheck.normalize(), basePath));

	}

}
