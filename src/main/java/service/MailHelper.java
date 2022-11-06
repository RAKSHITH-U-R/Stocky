package service;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailHelper {

    /**
     * Sends an HTML e-mail with inline images.
     * @param userName e-mail address of the sender's account
     * @param password password of the sender's account
     * @param toAddress e-mail address of the recipient
     * @param subject e-mail subject
     * @param htmlBody e-mail content with HTML tags
     * @param mapInlineImages
     *          key: Content-ID
     *          value: path of the image file
     * @throws AddressException
     * @throws MessagingException
     */

    private String userName;
    private String password;

    public MailHelper(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void send(String toAddress,
                     String subject, String htmlBody,
                     Map<String, String> mapInlineImages){
        // sets SMTP server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.user", userName);
            properties.put("mail.password", password);

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
            Session session = Session.getInstance(properties, auth);

            // creates a new e-mail message
            Message msg = new MimeMessage(session);
            try{
                msg.setFrom(new InternetAddress(userName));
                InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
                msg.setRecipients(Message.RecipientType.TO, toAddresses);
                msg.setSubject(subject);
                msg.setSentDate(new Date());

                // creates message part
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(htmlBody, "text/html");

                // creates multi-part
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // adds inline image attachments
                if (mapInlineImages != null && mapInlineImages.size() > 0) {
                    Set<String> setImageID = mapInlineImages.keySet();

                    for (String contentId : setImageID) {
                        MimeBodyPart imagePart = new MimeBodyPart();
                        imagePart.setHeader("Content-ID", "<" + contentId + ">");
                        imagePart.setDisposition(MimeBodyPart.INLINE);

                        String imageFilePath = mapInlineImages.get(contentId);
                        try {
                            imagePart.attachFile(imageFilePath);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        multipart.addBodyPart(imagePart);
                    }
                }

                msg.setContent(multipart);

                Transport.send(msg);
                System.out.println("Email Sent");
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}