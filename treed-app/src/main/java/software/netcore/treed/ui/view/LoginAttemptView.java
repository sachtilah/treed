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
import software.netcore.treed.ui.TreedCustomComponent;
import software.netcore.treed.ui.view.resetPassword.ResetPasswordView;

import java.util.*;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = LoginAttemptView.VIEW_NAME)
public class LoginAttemptView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/login/view";

    private final AccountService accountService;
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

        TextField usernameField = new MTextField(getString("username"));
        PasswordField passwordField = new PasswordField(getString("password"));
        Button loginButton = new MButton(getString("login")).withListener(clickEvent -> {
            login(usernameField.getValue(), passwordField.getValue());

        });

        Button resetPasswordButton = new Button(getString("resetPassword"));
        resetPasswordButton.addClickListener(
                event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));


        content.setSizeFull();
        content.addComponents(usernameField, passwordField, loginButton, resetPasswordButton);
        content.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(resetPasswordButton, Alignment.MIDDLE_CENTER);
    }

    private void login(String username, String password) {

        Iterable<Account> accounts = accountService.getAccounts();
        Collection<Account> accountCollection = new ArrayList<>();
        for (Account account : accounts) {
            accountCollection.add(account);
        }

        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account iter = iterator.next();
            if ((iter.getUsername().equals(username)) && (iter.getPassword().equals(password))) {
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME);
                break;
            } else Notification.show(getString("ntfWrongPassword"));
            iterator.remove();

        }
    }
}
