package software.netcore.treed;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;
import org.vaadin.viritin.v7.fields.MPasswordField;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.AccountRole;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @since v. 1.4.0
 */
@SpringUI
@Title("Treed")
@Push(transport = Transport.WEBSOCKET)
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
@RequiredArgsConstructor
public class RootUI extends UI {

    /* used to manipulate with accounts */
    private final AccountService accountService;

    @Override
    protected void init(VaadinRequest request) {
        getUI().setResizeLazy(true);
        setContent(new MVerticalLayout());
        build();
    }

    @Override
    public MVerticalLayout getContent() {
        return (MVerticalLayout) super.getContent();
    }

    /**
     * Build page.
     */
    private void build() {
        MVerticalLayout content = getContent();
        content.removeAllComponents();
        content.with(buildAddAccountWidget(), buildAccountTable());
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
