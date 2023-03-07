package inverview.returnandexist;

public class TestTryAndExist {
	public static void main(String[] args) {
	 try {
		 throw new IllegalAccessException("Test");
	 } catch(Exception E) {
//		 return;
		 System.exit(0);
	 }
	 finally {
//		 System.out.println("Finally is executed after return");
		 System.out.println("Finally is executed after exist");
	 }
	}

}
