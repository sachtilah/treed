package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import software.netcore.treed.data.converter.StringToHashConverter;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.repository.OtpRepository;
import software.netcore.treed.data.repository.sim.SentenceRepository;
import software.netcore.treed.data.repository.sim.PiktogramRepository;


/**
 * @author Jozef Petrik
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class ServiceConfiguration extends WebMvcConfigurerAdapter {

    private final AccountRepository accountRepo;
    private final OtpRepository otpRepo;
    private final PiktogramRepository piktogramRepo;
    private final SentenceRepository storyRepo;

    @Bean
    public AccountService accountService() {
        return new AccountService(passwordEncoder(), accountRepo);
    }

    @Bean
    public OtpService otpService() {
        return new OtpService(otpRepo);
    }

    @Bean
    public PiktogramService piktogramService() {
        return new PiktogramService(piktogramRepo);
    }

    @Bean
    public SentenceService storyService() {
        return new SentenceService(storyRepo);
    }

    /**
     * Bean used to compare human readable password with hashed one from database during login attempt.
     *
     * @return password encoder
     * @see StringToHashConverter
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
