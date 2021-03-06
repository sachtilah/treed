package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import software.netcore.treed.business.puzzle.PictogramPartService;
import software.netcore.treed.business.puzzle.PictogramPuzzleService;
import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.business.sim.PiktogramService;
import software.netcore.treed.data.converter.StringToHashConverter;
import software.netcore.treed.data.repository.AccountRepository;
import software.netcore.treed.data.repository.OtpRepository;
import software.netcore.treed.data.repository.puzzle.PictogramPartRepository;
import software.netcore.treed.data.repository.puzzle.PictogramPuzzleRepository;
import software.netcore.treed.data.repository.sim.ClauseRepository;
import software.netcore.treed.data.repository.sim.PiktogramRepository;


/**
 * @author Jozef Petrik
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class ServiceConfiguration implements WebMvcConfigurer {

    private final AccountRepository accountRepo;
    private final OtpRepository otpRepo;
    private final PiktogramRepository piktogramRepo;
    private final ClauseRepository clauseRepo;
    private final PictogramPartRepository pictogramPartRepo;
    private final PictogramPuzzleRepository pictogramPuzzleRepo;

    @Bean
    public PictogramPartService pictogramPartService() {
        return new PictogramPartService(pictogramPartRepo);
    }

    @Bean
    public PictogramPuzzleService pictogramPuzzleService() {
        return new PictogramPuzzleService(pictogramPuzzleRepo);
    }

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
    public ClauseService clauseService() {
        return new ClauseService(clauseRepo);
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
