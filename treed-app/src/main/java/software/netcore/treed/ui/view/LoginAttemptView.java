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

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

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


        TextField usernameField = new MTextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new MButton("Login").withListener(clickEvent -> {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:C:${file.separator}ProgramData${file.separator}Treed${file.separator}hsql${file.separator}db", "treed", "treed");
                Statement st = conn.createStatement();
                ResultSet rec = st.executeQuery("SELECT username, password FROM account");

                while (rec.next()) {
                    String x = usernameField.getValue();
                    String y = passwordField.getValue();


                    if (x.equals(rec.getString("username"))) {
                        if (y.equals(rec.getString("password"))) {
                            System.out.println("Logged in!");
                            getUI().getNavigator().navigateTo(CustomComponentView.VIEW_NAME);
                            break;
                        } else {
                            System.out.println("Password did not match username!");
                        }
                    } else {
                    System.out.println("Username did not match the database");
                    }
                }

                st.close();
            } catch(SQLException d) {
                System.out.println(d.toString());
            } catch(ClassNotFoundException f) {
                System.out.println(f.toString());
            }

        });

        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));

        Iterable<Account> accounts = accountService.getAccounts();
        Collection<Account> accountCollection = new ArrayList<>();
        for (Account account : accounts) {
            accountCollection.add(account);
        }
        Grid<Account> grid = new Grid<>("Accounts");
        grid.setItems(accountCollection);
        grid.addColumn(Account::getUsername).setCaption("Username");
        grid.addColumn(Account::getPassword).setCaption("Password");
        grid.addColumn(Account::getUserMail).setCaption("Mail");

        content.addComponents(usernameField, passwordField, loginButton, resetPasswordButton, grid);
        content.setComponentAlignment(usernameField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(resetPasswordButton, Alignment.MIDDLE_CENTER);
    }
}
