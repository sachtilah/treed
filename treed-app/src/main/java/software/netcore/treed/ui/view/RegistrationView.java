package software.netcore.treed.ui.view;

import com.vaadin.data.*;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.AccountRole;

import java.util.Objects;

/**
 * @since v.1.7.0
 */
@RequiredArgsConstructor
@SpringView(name = RegistrationView.VIEW_NAME)
public class RegistrationView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/registration";

    private final AccountService accountService;

    private MVerticalLayout mainLayout;

    private Binder<Account> binder;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new MVerticalLayout()
                .withFullSize()
                .withMargin(false);
        setCompositionRoot(this.mainLayout);
        setSizeFull();
        build();
    }

    private void build() {
        MTextField nameField = new MTextField(getString("registration-username-field"))
                .withWidth(20, Unit.EM);

        MTextField emailField = new MTextField(getString("registration-email-input-field"))
                .withWidth(20, Unit.EM);

        PasswordField passwordField = new PasswordField(getString("registration-password-field"));
        passwordField.setWidth(20, Unit.EM);

        PasswordField repeatPasswordField = new PasswordField(getString("registration-repeat-password-field"));
        repeatPasswordField.setWidth(20, Unit.EM);

        MButton backToLoginViewButton = new MButton()
                .withCaption(getString("registration-back-to-login-button"))
                .withStyleName(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY)
                .withListener(event -> getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        MButton createAccountButton = new MButton()
                .withCaption(getString("registration-create-account-button"))
                .withListener(event -> createAccount());

        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .withDefaultComponentAlignment(Alignment.MIDDLE_CENTER)
                .add(new MLabel()
                        .withContent(getString("registration-view-caption"))
                        .withStyleName(ValoTheme.LABEL_H1, ValoTheme.LABEL_BOLD))
                .add(nameField)
                .add(emailField)
                .add(passwordField)
                .add(repeatPasswordField)
                .add(new MHorizontalLayout()
                        .add(backToLoginViewButton)
                        .add(createAccountButton));

        binder = new BeanValidationBinder<>(Account.class);
        binder.forField(nameField)
                .withValidator((Validator<String>) (value, context) -> {
                    String errorMessage = isValid(value);
                    if (Objects.isNull(errorMessage)) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(errorMessage);
                    }
                })
                .asRequired(getString("registration-notification-username-required"))
                .bind("username");
        binder.forField(emailField)
                .withValidator(new EmailValidator(getString("registration-notification-invalid-email")))
                .bind("userMail");
        binder.forField(passwordField)
                .withValidator(new StringLengthValidator(getString("registration-notification-password-required"), 3, 50))
                .bind("password");
        binder.forField(repeatPasswordField)
                .withValidator((Validator<String>) (value, context) -> {
                    if (Objects.isNull(value)) {
                        return ValidationResult.error(getString("registration-notification-repeat-password-required"));
                    }

                    return value.equals(passwordField.getValue()) ?
                            ValidationResult.ok() :
                            ValidationResult.error(getString("registration-notification-repeat-password-not-match"));
                })
                .bind((ValueProvider<Account, String>) account -> null,
                        (Setter<Account, String>) (account, s) -> {// empty setter
                        });

        Account acc = new Account();
        acc.setRole(AccountRole.STUDENT);
        binder.setBean(acc);

        mainLayout.removeAllComponents();
        mainLayout.add(content, Alignment.TOP_CENTER);
    }

    /**
     * Validate given {@code username}.
     *
     * @param username username
     * @return error message or null if validation passed
     */
    private String isValid(String username) {
        if (Objects.isNull(username)) {
            return getString("registration-notification-username-required");
        }
        username = username.trim();

        if (username.length() < 3) {
            return getString("registration-notification-length-invalid");
        }

        if (accountService.isUsernameUsed(username)) {
            return getString("registration-notification-username-used");
        }

        return null;
    }


    private void createAccount() {
        if (binder.isValid()) {
            Account acc = binder.getBean();
            boolean created = accountService.createAccount(acc);
            if (created) {
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME);
            } else {
                Notification.show(getString("registration-notification-invalid"));
            }
        } else {
            Notification.show(getString("registration-notification-invalid"));
        }
    }

}
