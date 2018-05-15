package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepo;

    @Transactional
    public Iterable<Account> getAccounts() {
        log.info("Getting all account");
        return accountRepo.findAll();
    }

    @Transactional
    public void updatePasswordAccount(Account account) {
        log.info("Updating account {}", account);
        accountRepo.save(account);
    }

    /**
     * Login user. After successful login attempt an {@link Account} is returned.
     *
     * @param username username
     * @param password password
     * @return account if logged in, otherwise {@literal null}
     */
    @Transactional
    public Account login(String username, String password) {
        log.info("Logging in {} : {}", username, password);
        Optional<Account> optionalAccount = accountRepo.findByUsername(username);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            if (passwordEncoder.matches(password, account.getPassword())) {
                return account;
            }

        } else {
            log.info("User with this name not found.");
        }

        return null;
    }

    /**
     * Returns true if given {@code username} is NOT used yet.
     *
     * @param username username
     * @return true if NOT used, otherwise false
     */
    @Transactional
    public boolean isUsernameUsed(String username) {
        return accountRepo.findByUsername(username).isPresent();
    }

    @Transactional
    public boolean createAccount(Account acc) {
        log.info("Creating new {}", acc);
        try {
            accountRepo.save(acc);
        } catch (Exception e) {
            log.warn("Failed to create account", e);
            return false;
        }

        return true;
    }

}
