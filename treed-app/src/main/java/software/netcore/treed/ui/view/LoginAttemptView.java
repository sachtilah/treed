package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.ui.view.resetPassword.ResetPasswordView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = LoginAttemptView.VIEW_NAME)
public class LoginAttemptView extends CustomComponent implements View {

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

        TextField usernameField = new MTextField("username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new MButton("Prihlásiť").withListener(clickEvent -> {

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
                else Notification.show("Nesprávne používateľské meno alebo heslo!");
                iterator.remove();
            }
        });

        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));
        content.setSizeFull();
        content.addComponents(usernameField, passwordField, loginButton, resetPasswordButton);
        content.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(resetPasswordButton, Alignment.MIDDLE_CENTER);
    }
}
