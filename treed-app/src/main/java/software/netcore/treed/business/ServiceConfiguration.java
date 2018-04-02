package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.repository.OtpRepository;

import java.util.Locale;

/**
 * @author Jozef Petrik
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class ServiceConfiguration extends WebMvcConfigurerAdapter {

    private final AccountRepository accountRepo;
    private final OtpRepository otpRepo;

    @Bean
    public AccountService accountService() {
        return new AccountService(accountRepo);
    }

    @Bean
    public OtpService otpService() {
        return new OtpService(otpRepo);
    }

}
