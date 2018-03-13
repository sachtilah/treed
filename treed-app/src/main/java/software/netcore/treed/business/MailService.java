package software.netcore.treed.business;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author alejandro@vaadin.com
 **/
public class MailService {

    /**
     * Sends an email message with no attachments.
     *
     * @param from       email address from which the message will be sent.
     * @param recipients the recipients of the message.
     * @param subject    subject header field.
     * @param text       content of the message.
     * @throws MessagingException
     * @throws IOException
     */

    public static void send(String from, String recipients, String subject, String text)
            throws EmailException {

        // check for null references
        Objects.requireNonNull(from);
        Objects.requireNonNull(recipients);

        HtmlEmail email = new HtmlEmail();

        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setSSLOnConnect(true);
        email.setAuthentication("filip.ondra000@gmail.com","gtasanandreas");
        email.setFrom(from);
        email.addTo(recipients);
        email.setSubject(subject);
        email.setHtmlMsg(text);

        email.send();
    }
}