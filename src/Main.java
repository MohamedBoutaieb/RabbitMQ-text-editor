
public class Main {
    public static void main(String[] s ) throws Exception{
        TextEditor t= new TextEditor();
        User.requestDisplay("section1");
        User.requestDisplay("section2");
        if (!User.singleton){
            User.RequestSection1();
            User.RequestSection2();
            User.singleton=true;
        }
    }
}
