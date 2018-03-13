package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.business.MailService;
import software.netcore.treed.data.schema.Account;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

/**
 * @since v. 1.0.0
 */
@SpringView(name = ResetPasswordView.VIEW_NAME)
public class ResetPasswordView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/reset/mailview";

    public ResetPasswordView() {
        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

        // Compose from multiple components
        Label label = new Label("Zadajte mail:");
        panelContent.addComponent(label);

        TextField mailField = new TextField("E-mail");
        panelContent.addComponent(mailField);

        Button backButton = new MButton("Odoslať").withListener(clickEvent -> {
            //backButton.witaddClickListener((Button.ClickListener) event ->
            sendMail(mailField.getValue());
        });


        panelContent.addComponent(backButton);
        // The composition root MUST be set
        setCompositionRoot(panelContent);

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        // this is not needed for a Composite
        setSizeUndefined();

        panelContent.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

    }

    private void sendMail(String to) {
        try {

            String resetPass = generateOTP(6);

            // all values as variables to clarify its usage
            String from = "filip.ondra000@gmail.com";
            String subject = "Treed Reset Password";
            String text = "Here is link to reset password: " + "http://localhost:8080/#!/reset/verifyview/" + resetPass;

            // call the email service to send the message
            MailService.send(from, to, subject, text);

            //getUI().getNavigator().navigateTo(VerifyPasswordView.VIEW_NAME);
            //addNewPass(to, resetPass);

            addNewPass(to, resetPass);

            Notification.show("Email sent");

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Error sending the email", Notification.Type.ERROR_MESSAGE);
        }
    }

    public static String generateOTP(int length) {
        String numbers = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        char[] generatedOTP = new char[length];
        for (int i = 0; i < length; i++)
            generatedOTP[i] = numbers.charAt(random.nextInt(numbers.length()));
        String otp = String.valueOf(generatedOTP);

        return otp;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


    public void addNewPass(String to, String otpPass) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:C:${file.separator}ProgramData${file.separator}Treed${file.separator}hsql${file.separator}db", "treed", "treed");

            PreparedStatement create = conn.prepareStatement("CREATE TABLE IF NOT EXISTS otp(usermail VARCHAR(255), " +
                    "otpPass VARCHAR (255), tstamp TIMESTAMP NOT NULL, PRIMARY KEY(usermail))");

            //drop table if you need
            //          PreparedStatement create = conn.prepareStatement("DROP TABLE otp");

            create.executeUpdate();

            PreparedStatement fill = conn.prepareStatement("INSERT INTO otp (usermail, otpPass, tstamp)" +
                     " VALUES (?, ?, ?)");
            fill.setString(1, to);
            fill.setString(2, otpPass);
            fill.setTimestamp(3, new Timestamp(new Date().getTime()));
            fill.executeUpdate();

            System.out.println("all executed");
        } catch (SQLException d) {
            System.out.println(d.toString());
        } catch (ClassNotFoundException f) {
            System.out.println(f.toString());
        }
    }
}

