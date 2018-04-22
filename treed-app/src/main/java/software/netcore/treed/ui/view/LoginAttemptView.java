package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.Otp;
import software.netcore.treed.ui.view.resetPassword.ResetPasswordView;

import java.util.*;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = LoginAttemptView.VIEW_NAME)
public class LoginAttemptView extends CustomComponent implements View {

    @Autowired
    private MessageSource messageSource;

    public static final String VIEW_NAME = "/login/view";
    private final AccountService accountService;
    private final OtpService otpService;
    private VerticalLayout mainLayout;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new VerticalLayout();
        setCompositionRoot(this.mainLayout);
        build();
    }

    /**
     * Build page.
     */
    private void build() {
        VerticalLayout content = this.mainLayout;
        content.removeAllComponents();

        Locale locale = VaadinSession.getCurrent().getLocale();
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        TextField usernameField = new MTextField(messages.getString("username"));
        PasswordField passwordField = new PasswordField(messages.getString("password"));
        Button loginButton = new MButton(messages.getString("login")).withListener(clickEvent -> {

            Iterable<Account> accounts = accountService.getAccounts();
            Collection<Account> accountCollection = new ArrayList<>();
            for (Account account : accounts) {
                accountCollection.add(account);
            }

            Iterator<Account> iterator = accounts.iterator();
            while(iterator.hasNext()){
                Account iter = iterator.next();
                if((iter.getUsername().equals(usernameField.getValue())) && (iter.getPassword().equals(passwordField.getValue()))){
                    getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME);
                    break;
                }
                else Notification.show(messages.getString("ntfWrongPassword"));
                iterator.remove();
            }
        });

        Button resetPasswordButton = new Button(messages.getString("resetPassword"));
        resetPasswordButton.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));

        Iterable<Account> accounts2 = accountService.getAccounts();
        Collection<Account> account2Collection = new ArrayList<>();
        for (Account account : accounts2) {
            account2Collection.add(account);
        }
        Grid<Account> grid = new Grid<>("Accounts");
        grid.setItems(account2Collection);
        grid.addColumn(Account::getUsername).setCaption("Username");
        grid.addColumn(Account::getPassword).setCaption("Password");
        grid.addColumn(Account::getUserMail).setCaption("Mail");

        Iterable<Otp> otps = otpService.getOtps();
        Collection<Otp> otpCollection = new ArrayList<>();
        for (Otp otp : otps) {
            otpCollection.add(otp);
        }
        Grid<Otp> grid2 = new Grid<>("Otps");
        grid2.setItems(otpCollection);
        grid2.addColumn(Otp::getUsermail).setCaption("Usermail");
        grid2.addColumn(Otp::getOtpPass).setCaption("OtpPass");


        content.setSizeFull();
        content.addComponents(usernameField, passwordField, loginButton, resetPasswordButton, grid, grid2);
        content.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(resetPasswordButton, Alignment.MIDDLE_CENTER);
    }
}
