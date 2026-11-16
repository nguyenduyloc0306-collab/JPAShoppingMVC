package vn.iotstar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {
    private static final String FROM;
    private static final String PASS;

    static {
        String u = System.getenv("MAIL_USER");
        String p = System.getenv("MAIL_APP_PASSWORD");
        if (u == null || p == null) {
            try (InputStream in = EmailUtil.class.getClassLoader()
                    .getResourceAsStream("mail.properties")) {
                if (in != null) {
                    Properties cfg = new Properties();
                    cfg.load(in);
                    if (u == null) u = cfg.getProperty("mail.user");
                    if (p == null) p = cfg.getProperty("mail.password");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FROM = u == null ? null : u.trim();
        PASS = p == null ? null : p.replace(" ", "");   // App Password hay bị dán kèm dấu cách
    }

    public static void send(String to, String subject, String body) throws MessagingException {
        if (FROM == null || FROM.isEmpty() || PASS == null || PASS.isEmpty()) {
            throw new MessagingException(
                "Chưa cấu hình tài khoản gửi mail (MAIL_USER/MAIL_APP_PASSWORD hoặc mail.properties)");
        }

        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.connectiontimeout", "10000");
        p.put("mail.smtp.timeout", "10000");
        p.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASS);
            }
        });

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        Transport.send(msg);
    }
}