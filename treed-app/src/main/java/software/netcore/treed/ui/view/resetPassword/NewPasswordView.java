package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.api.AbstractFreeEnterView;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.Otp;

import java.util.*;

/**
 * @since v. 1.0.0
 */
@SpringView(name = NewPasswordView.VIEW_NAME)
public class NewPasswordView extends AbstractFreeEnterView implements View {

    public static final String VIEW_NAME = "/reset/verifyview";
    private final OtpService otpService;
    private final AccountService accountService;

    public String newPass;
    public String parameter;

    public NewPasswordView(OtpService otpService, AccountService accountService) {
        this.otpService = otpService;
        this.accountService = accountService;
    }

    public void build(){
        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

        // Compose from multiple components
        Label label = new Label(getString("newPassword-set-password-label"));
        panelContent.addComponent(label);

        PasswordField passwordField = new PasswordField(getString("newPassword-new-password-field"));
        panelContent.addComponent(passwordField);


        Button verifyButton = new MButton(getString("newPassword-change-password-field")).withListener(clickEvent -> {
          newPass = passwordField.getValue();
          writePassword(parameter, newPass);

        });

        panelContent.addComponent(verifyButton);
        // The composition root MUST be set
        setCompositionRoot(panelContent);

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        // this is not needed for a Composite
        setSizeUndefined();

        panelContent.setComponentAlignment(verifyButton, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    }

    public void writePassword(String otpPass, String newPass) {

        boolean isHere = false;

        Iterable<Otp> otps = otpService.getOtps();
        Collection<Otp> otpCollection = new ArrayList<>();
        for (Otp otp : otps) {
            otpCollection.add(otp);
        }

        Iterable<Account> accounts = accountService.getAccounts();
        Collection<Account> accountCollection = new ArrayList<>();
        for (Account account : accounts) {
            accountCollection.add(account);
        }

        Iterator<Otp> iteratorOtp = otps.iterator();
        Iterator<Account> iteratorAccount = accounts.iterator();
        while(iteratorOtp.hasNext()){
            Otp iterOtp = iteratorOtp.next();
            //expirationDate
            int MILLIS_PER_DAY = 20*60*1000;
            Calendar expirationTime = Calendar.getInstance();
            expirationTime.setTime(iterOtp.getCreateTime());
            expirationTime.add(Calendar.MILLISECOND, MILLIS_PER_DAY);
            Calendar today = Calendar.getInstance();

            if(iterOtp.getOtpPass().equals(otpPass)){
                while(iteratorAccount.hasNext()){
                    Account iterAccount = iteratorAccount.next();
                    if((iterAccount.getUserMail().equals(iterOtp.getUsermail())) && (today.before(expirationTime))){
                        iterAccount.setPassword(newPass);
                        accountService.updatePasswordAccount(iterAccount);
                        otpService.deleteOtp(iterOtp);
                        Notification.show(getString("newPassword-notification-password-changed"));
                    }
                    else {
                        otpService.deleteOtp(iterOtp);
//                        Notification.show(messages.getString("otpExpired"));
                    }
                }
            }
            if (!isHere) {
                Notification.show(getString("newPassword-notification-otp-not-found"));
                iteratorOtp.remove();
            }
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event){
        build();
        if(event.getParameters() != null){
            String[] params = event.getParameters().split("/");
            for(String param : params)
                parameter = param;
        }
    }
}
