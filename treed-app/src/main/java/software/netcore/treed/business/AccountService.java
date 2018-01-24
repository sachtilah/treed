package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepo;

    public Iterable<Account> getAccounts() {
        log.info("Getting all account");
        return accountRepo.findAll();
    }

    public void saveAccount(Account account) {
        log.info("Saving new account {}", account);
        accountRepo.save(account);
    }

    public Account login(String username, String password) {

        Optional<Account> optionalAccount = accountRepo.findByUsername(username);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getPassword().equals(password)) {
                return account;
            }
        }

        return null;
    }

}
