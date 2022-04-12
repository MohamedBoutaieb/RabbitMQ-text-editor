import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {

  public static String getFileContent(String name) {
    String s ="";
    try {
      File myObj = new File("C:\\my-workspace\\TextEditor2\\src\\"+name);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        s+=data;
        s+="\n";
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return s;
  }
}
