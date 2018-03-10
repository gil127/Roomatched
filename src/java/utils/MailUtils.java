/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import pojos.User;


/**
 *
 * @author Gil
 */
public class MailUtils {

    public static boolean sendTo(String senderName, String recpientName, String recipientEmail, String subject, String mailContent, Boolean isHtml) {
        List<String> errors = checkEmailValidation(recipientEmail);
        if (errors != null && !errors.isEmpty()) {
            return false;
        }
        
        int PORT = 587;

        String HOST_NAME = "smtp.gmail.com";
        String SENDER_ADDRESS = "admin@roomatched.com";
        String ROOMATCHED_MAILBOX = "roomatched@gmail.com";
        String ROOMATCHED_MAILBOX_PASSWORD = "webstud1!";

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", HOST_NAME);
            props.put("mail.smtp.socketFactory.port", PORT);
            props.put("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.ssl.enable", "false");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", HOST_NAME);

            Session mailSession = Session.getDefaultInstance(props, null);
            MimeMessage mailMessage = new MimeMessage(mailSession);
            mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mailMessage.setSubject(subject);
            mailMessage.setContent(mailContent, isHtml? "text/html; charset=UTF-8" : "text/plain; charset=UTF-8");

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(HOST_NAME, ROOMATCHED_MAILBOX, ROOMATCHED_MAILBOX_PASSWORD);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            transport.close();
            
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static List<String> checkEmailValidation(String email) {
        List<String> errors = new ArrayList<String>();
        
        if (email == null || email.isEmpty()) {
            return errors;
        }
        
        if (!email.contains("@")) {
            errors.add("Your email adderss should contain '@'");
        }
        
        if (!email.contains(".")) {
            errors.add("Your email adderss should contain '.'");
        }
        
        if (MySQLUtils.emailExist(User.class, email)) {
            errors.add("Your email adderss already exist");
        }
        
        return errors;
    }
}