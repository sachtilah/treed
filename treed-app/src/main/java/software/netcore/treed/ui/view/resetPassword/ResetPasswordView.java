package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.business.MailService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.data.schema.Otp;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

/**
 * @since v. 1.0.0
 */
@SpringView(name = ResetPasswordView.VIEW_NAME)
public class ResetPasswordView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/reset/mailview";
    private final OtpService otpService;


    public ResetPasswordView(OtpService otpService) {
        this.otpService = otpService;
        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

        // Compose from multiple components
        Label label = new Label("Zadajte mail:");
        panelContent.addComponent(label);

        TextField mailField = new TextField("E-mail");
        panelContent.addComponent(mailField);

        Button backButton = new MButton("Odoslať").withListener(clickEvent -> {
            EmailValidator emailValidator = new EmailValidator();
            if(emailValidator.isValid(mailField.getValue(), null))
                sendMail(mailField.getValue());
            else Notification.show("Nesprávna e-mailová adresa!");
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
            addNewPass(to, resetPass);

            Notification.show("Email sent");

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Error sending the email", Notification.Type.ERROR_MESSAGE);
        }
    }

    private static String generateOTP(int length) {
        String numbers = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        char[] generatedOTP = new char[length];
        for (int i = 0; i < length; i++)
            generatedOTP[i] = numbers.charAt(random.nextInt(numbers.length()));

        return String.valueOf(generatedOTP);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }


    private void addNewPass(String to, String otpPass) {

        Otp otp = new Otp();
        if (to.isEmpty()) {
            Notification.show("Zadajte emailovú adresu.");
        } else {
            otp.setUsermail(to);
            otp.setOtpPass(otpPass);
            otp.setCreateTime(Date.from(Instant.now()));

            // save account
            otpService.saveOtp(otp);

        }
    }
}

