package software.netcore.treed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @since v. 1.4.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepo;

    public Collection<Account> getAccounts() {

//        return accountRepo.findAll();

        Account acc1 = new Account();
        acc1.setUsername("Acc1");

        Account acc2 = new Account();
        acc2.setUsername("Acc2");

        return Arrays.asList(acc1, acc2);
    }


}
