package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.netcore.treed.data.repository.AccountRepository;

/**
 * @author Jozef Petrik
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class ServiceConfiguration {

    private final AccountRepository accountRepo;


    @Bean
    public AccountService accountService() {
        return new AccountService(accountRepo);
    }


}
