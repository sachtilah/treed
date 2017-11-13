package software.netcore.treed.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data configuration class.
 *
 * @since v. 1.4.0
 */
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@EntityScan
@Configuration
public class DataConfiguration {
}
