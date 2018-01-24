package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.AccountRole;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = ExampleAccountView.VIEW_NAME)
public class ExampleAccountView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/example/view";

    private final AccountService accountService;
    private VerticalLayout mainLayout;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(this.mainLayout);
        build();
    }

    /**
     * Build page.
     */
    private void build() {
        VerticalLayout content = this.mainLayout;
        content.removeAllComponents();

        Button navigationButton = new Button("Go to component view");
        navigationButton.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(CustomComponentView.VIEW_NAME));

        content.addComponent(navigationButton);
        content.setComponentAlignment(navigationButton, Alignment.BOTTOM_CENTER);

        content.addComponents(buildAddAccountWidget(), buildAccountTable());
    }

    private Component buildAddAccountWidget() {
        TextField usernameField = new MTextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        ComboBox<AccountRole> accountRoleComboBox = new ComboBox<>("Role",
                Arrays.asList(AccountRole.values()));


        accountRoleComboBox.setEmptySelectionAllowed(false);
        accountRoleComboBox.setSelectedItem(AccountRole.ADMINISTRATOR);

        Button createButton = new MButton("Create")
                .withListener(e -> {

                    // create new account
                    Account account = new Account();
                    account.setUsername(usernameField.getValue());
                    account.setPassword(passwordField.getValue());
                    account.setRole(accountRoleComboBox.getSelectedItem().get());
                    account.setCreateTime(Date.from(Instant.now()));

                    // save account
                    accountService.saveAccount(account);

                    // rebuild UI
                    build();
                });




        return new MVerticalLayout()
                .with(usernameField)
                .with(passwordField)
                .with(accountRoleComboBox)
                .with(createButton);
    }

    private Component buildAccountTable() {
        Iterable<Account> accounts = accountService.getAccounts();

        Collection<Account> accountCollection = new ArrayList<>();
        for (Account account : accounts) {
            accountCollection.add(account);
        }

        Grid<Account> grid = new Grid<>("Accounts");
        grid.setItems(accountCollection);

        grid.addColumn(Account::getUsername).setCaption("Username");
        grid.addColumn(Account::getPassword).setCaption("Password");
        grid.addColumn(Account::getRole).setCaption("Role");

        return grid;
    }

}
