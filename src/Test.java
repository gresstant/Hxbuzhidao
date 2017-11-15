import java.awt.event.*;
import javax.swing.*;
import java.util.regex.*;

public class Test {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[123]");
        Matcher matcher = pattern.matcher("+123451111");
        System.out.println(matcher.groupCount());
        //System.out.println(matcher.group());
        for (int i = 0; i < matcher.groupCount(); i++) {
            System.out.println(matcher.group(i));
        }
    }
}
