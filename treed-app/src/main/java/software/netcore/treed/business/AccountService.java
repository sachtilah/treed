package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public Iterable<Account> getAccounts() {
        log.info("Getting all account");
        return accountRepo.findAll();
    }

    public void saveAccount(Account account) {
        log.info("Saving new account {}", account);
        accountRepo.save(account);
    }

    /**
     * Login user. After successful login attempt an {@link Account} is returned.
     *
     * @param username username
     * @param password password
     * @return account if logged in, otherwise {@literal null}
     */
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

}
