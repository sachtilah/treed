package software.netcore.treed.ui.view;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.ui.AuthenticationProvider;

/**
 * @since v.1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class VaadinConfiguration {

    private final AccountService accountService;

    @Bean
    AuthenticationProvider authenticationProvider(){
        return new AuthenticationProvider(accountService);
    }

}
