package software.netcore.treed.ui;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.data.schema.Account;

/**
 * @since v. 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class AuthenticationProvider {

    private final AccountService accountService;

    /**
     * Authenticate given username/password using {@code accountService}. If attempt succeed
     * store account object into session for easy access.
     *
     * @param username username
     * @param password password
     * @return true if attempt has been successful, otherwise false
     */
    public boolean login(@NonNull String username, @NonNull String password) {
        log.debug("Authenticating user '{}'", username);

        Account account = accountService.login(username, password);
        if (account == null) {
            return false;
        }

        VaadinSession vaadinSession = VaadinSession.getCurrent();
        vaadinSession.setAttribute("account", account);
        return true;

    }

    /**
     * Logout user.
     */
    public static void logout() {
        VaadinSession.getCurrent().getSession().invalidate();
        Page.getCurrent().reload();
    }

    /**
     * Check whether user is logged in.
     *
     * @return true if user is logged in, otherwise false
     */
    public static boolean isLoggedIn() {
        Account account = (Account) VaadinSession.getCurrent().getAttribute("account");
        return account != null;
    }

    /**
     * Returns logged user.
     *
     * @return logged user or {@code null}
     */
    public static Account getLoggedAccount() {
        if (VaadinSession.getCurrent() != null) {
            return (Account) VaadinSession.getCurrent().getAttribute("account");
        }

        return null;
    }

}
