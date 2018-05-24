package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.api.AbstractFreeEnterView;
import software.netcore.treed.business.MailService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.data.schema.Otp;

import java.time.Instant;
import java.util.*;

/**
 * @since v. 1.0.0
 */
@SpringView(name = ResetPasswordView.VIEW_NAME)
public class ResetPasswordView extends AbstractFreeEnterView implements View {

    public static final String VIEW_NAME = "/reset/mailview";
    private final OtpService otpService;


    public ResetPasswordView(OtpService otpService) {
        this.otpService = otpService;
    }

    public void build(){
        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

              // Compose from multiple components
        Label label = new Label(getString("resetPassword-enter-email-label"));
        panelContent.addComponent(label);

        TextField mailField = new TextField("E-mail");
        panelContent.addComponent(mailField);

        Button sendButton = new MButton(getString("resetPassword-send-button")).withListener(clickEvent -> {
            EmailValidator emailValidator = new EmailValidator();
            if(emailValidator.isValid(mailField.getValue(), null))
                sendMail(mailField.getValue());
            else Notification.show(getString("resetPassword-notification-wrong-email"));
        });


        panelContent.addComponent(sendButton);
        // The composition root MUST be set
        setCompositionRoot(panelContent);

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        // this is not needed for a Composite
        setSizeUndefined();

        panelContent.setComponentAlignment(sendButton, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

    }

    private void sendMail(String to) {
        try {
            boolean isHere = false;
            String resetPass = generateOTP(6);

            // all values as variables to clarify its usage
            String from = "treedapplication@gmail.com";
            String subject = "Treed Reset Password";
            String text = getString("resetPassword-email-body") + "http://localhost:8080/#!/reset/verifyview/" + resetPass;

            Iterable<Otp> otps = otpService.getOtps();
            Collection<Otp> otpCollection = new ArrayList<>();
            for (Otp otp : otps) {
                otpCollection.add(otp);
            }
            Iterator<Otp> iteratorOtp = otps.iterator();

            // call the email service to send the message
            if (to.isEmpty()) {
                Notification.show(getString("resetPassword-notification-empty-email"));
            } else {
                while (iteratorOtp.hasNext()) {
                    Otp iterOtp = iteratorOtp.next();
                    if (iterOtp.getUsermail().contains(to)) {
                        isHere = true;
                        Notification.show(getString("resetPassword-notification-otp-mail-used"));
                    }
                }
                if(!isHere){
                    addNewPass(to, resetPass);
                    MailService.send(from, to, subject, text);
                    Notification.show(getString("resetPassword-notification-email-sent"));
                }
                if(otpCollection.isEmpty()){
                    addNewPass(to, resetPass);
                    MailService.send(from, to, subject, text);
                    Notification.show(getString("resetPassword-notification-email-sent"));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            Notification.show(getString("resetPassword-notification-email-error"), Notification.Type.ERROR_MESSAGE);
        }
    }

            private static String generateOTP ( int length){
                String numbers = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                Random random = new Random();

                char[] generatedOTP = new char[length];
                for (int i = 0; i < length; i++)
                    generatedOTP[i] = numbers.charAt(random.nextInt(numbers.length()));

                return String.valueOf(generatedOTP);
            }

            @Override
            public void enter (ViewChangeListener.ViewChangeEvent event){
                build();
            }


            private void addNewPass (String to, String otpPass){

                Otp otpAdd = new Otp();
                otpAdd.setUsermail(to);
                otpAdd.setOtpPass(otpPass);
                otpAdd.setCreateTime(Date.from(Instant.now()));
                otpService.saveOtp(otpAdd);
            }
}

