package inverview.nomura.task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileHelper {

	public static String APPLICATION_PROPERTIES = "/Users/liuyingjie/eclipse-workspace/leetcode/src/inverview/nomura/task3/application.properties";
	public static final String CSV_FILE_NAME_KEY = "csv.file.name";
	public static final String DEFAULT_FILE_NAME = "default.csv";

	public static String csvFileNameFromProperties() {
		// TODO: implement
		// NOTE: While working with this method an exception may be thrown.
		// Please catch any Exceptions, and return DEFAULT_FILE_NAME after Exception was
		// caught.
		String fileName="";
		try (InputStream input = new FileInputStream(APPLICATION_PROPERTIES)) {

            Properties prop = new Properties();
            prop.load(input);
            fileName = prop.getProperty(CSV_FILE_NAME_KEY);
        } catch (IOException ex) {
        	ex.printStackTrace();
        	return DEFAULT_FILE_NAME;
        }
		return fileName;
	}

}
