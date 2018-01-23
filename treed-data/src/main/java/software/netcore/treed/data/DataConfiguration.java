package software.netcore.treed.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data layer configuration class.
 *
 * @since v. 1.0.0
 */
@EntityScan
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@PropertySource(value = "classpath:application-data.properties")
@Configuration
public class DataConfiguration {
}
