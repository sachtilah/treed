package software.netcore.treed.ui.view;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.osgi.themes.ValoThemeContribution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.ui.AuthenticationProvider;
import software.netcore.treed.ui.TreedCustomComponent;
import software.netcore.treed.ui.view.resetPassword.ResetPasswordView;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = LoginAttemptView.VIEW_NAME)
public class LoginAttemptView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/login";

    private final AuthenticationProvider authenticationProvider;
    private MVerticalLayout mainLayout;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new MVerticalLayout()
                .withFullSize();
        setCompositionRoot(this.mainLayout);
        setSizeFull();
        build();
    }

    /**
     * Build page.
     */
    private void build() {
        TextField usernameField = new MTextField(getString("loginAttempt-username-field"))
                .withFullSize();
        PasswordField passwordField = new PasswordField(getString("loginAttempt-password-field"));
        passwordField.setSizeFull();

        Button loginButton = new MButton(getString("loginAttempt-login-button"))
                .withListener(clickEvent -> {
                    login(usernameField.getValue(), passwordField.getValue());
                })
                .withClickShortcut(ShortcutAction.KeyCode.ENTER);

        MButton accountRegistrationButton = new MButton(getString("loginAttempt-account-registration-button"))
                .withStyleName(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY)
                .withListener(event -> getUI().getNavigator().navigateTo(RegistrationView.VIEW_NAME));

        MButton resetPasswordButton = new MButton(getString("loginAttempt-reset-password-button"))
                .withStyleName(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY)
                .withListener(event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));

        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .add(usernameField, passwordField)
                .add(new MHorizontalLayout()
                        .add(new MVerticalLayout()
                                .withMargin(false)
                                .withSpacing(false)
                                .add(accountRegistrationButton)
                                .add(resetPasswordButton))
                        .add(loginButton, Alignment.MIDDLE_CENTER));

        mainLayout.removeAllComponents();
        mainLayout.add(content, Alignment.MIDDLE_CENTER);
    }

    /**
     * Login user. After successful login redirect to the home page. Unsuccessful login attempt
     * show notification popup.
     *
     * @param username username
     * @param password password
     */
    private void login(String username, String password) {
        boolean isLoggedIn = authenticationProvider.login(username, password);

        if (isLoggedIn) {
            getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME);
        } else {
            Notification.show(getString("loginAttempt-notification-wrong-inputs"));
        }
    }

}
