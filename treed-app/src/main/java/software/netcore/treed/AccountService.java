package software.netcore.treed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @since v. 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepo;

    Iterable<Account> getAccounts() {
        log.info("Getting all account");
        return accountRepo.findAll();
    }

    void saveAccount(Account account) {
        log.info("Saving new account {}", account);
        accountRepo.save(account);
    }
}
