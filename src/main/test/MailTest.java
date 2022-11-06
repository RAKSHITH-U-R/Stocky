import service.MailHelper;

import java.util.HashMap;
import java.util.Map;


public class MailTest {
    public static void main(String[] args) {
        String mailFrom = "zmechy2001@gmail.com";
        String password = "gREENARROW_01";

        // message info
        String mailTo = "rit6569@gmail.com";
        String subject = "Test e-mail with inline images";
        StringBuffer body
                = new StringBuffer("<html>This message contains two inline images.<br>");
        body.append("The first image is a chart:<br>");
        body.append("<img src=\"cid:image1\" width=\"30%\" height=\"30%\" /><br>");
        body.append("</html>");

        // inline images
        Map<String, String> inlineImages = new HashMap<String, String>();
        inlineImages.put("image1", "image.png");

        MailHelper mh = new MailHelper(mailFrom, password);
        mh.send(mailTo, subject, body.toString(), inlineImages);
        System.out.println("Email sent.");
    }
}
