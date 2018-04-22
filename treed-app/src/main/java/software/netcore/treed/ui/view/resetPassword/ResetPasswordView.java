package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.business.MailService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.Otp;
import software.netcore.treed.ui.TreedCustomComponent;

import java.time.Instant;
import java.util.*;

/**
 * @since v. 1.0.0
 */
@SpringView(name = ResetPasswordView.VIEW_NAME)
public class ResetPasswordView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/reset/mailview";
    private final OtpService otpService;


    public ResetPasswordView(OtpService otpService) {
        this.otpService = otpService;
    }

    public void build(){
        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

              // Compose from multiple components
        Label label = new Label(getString("enterEmail"));
        panelContent.addComponent(label);

        TextField mailField = new TextField("E-mail");
        panelContent.addComponent(mailField);

        Button backButton = new MButton(getString("send")).withListener(clickEvent -> {
            EmailValidator emailValidator = new EmailValidator();
            if(emailValidator.isValid(mailField.getValue(), null))
                sendMail(mailField.getValue());
            else Notification.show(getString("ntfWrongEmail"));
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
        Locale locale = VaadinService.getCurrentRequest().getLocale();
        this.getSession().setLocale(locale);
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        try {
            boolean isHere = false;
            String resetPass = generateOTP(6);

            // all values as variables to clarify its usage
            String from = "filip.ondra000@gmail.com";
            String subject = "Treed Reset Password";
            String text = messages.getString("emailBody") + "http://localhost:8080/#!/reset/verifyview/" + resetPass;

            Iterable<Otp> otps = otpService.getOtps();
            Collection<Otp> otpCollection = new ArrayList<>();
            for (Otp otp : otps) {
                otpCollection.add(otp);
            }
            Iterator<Otp> iteratorOtp = otps.iterator();

            // call the email service to send the message
            if (to.isEmpty()) {
                Notification.show(messages.getString("enterEmail"));
            } else {
                while (iteratorOtp.hasNext()) {
                    Otp iterOtp = iteratorOtp.next();
                    if (iterOtp.getUsermail().contains(to)) {
                        isHere = true;
                        Notification.show(messages.getString("ntfOtpMailUsed"));
                    }
                }
                if(!isHere){
                    addNewPass(to, resetPass);
                    MailService.send(from, to, subject, text);
                    Notification.show(messages.getString("emailSent"));
                }
                if(otpCollection.isEmpty()){
                    addNewPass(to, resetPass);
                    MailService.send(from, to, subject, text);
                    Notification.show(messages.getString("emailSent"));
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            Notification.show(messages.getString("emailError"), Notification.Type.ERROR_MESSAGE);
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

