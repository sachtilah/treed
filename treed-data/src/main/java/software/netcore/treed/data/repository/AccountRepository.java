package software.netcore.treed.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.schema.Account;

import java.util.Collection;
import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    @Transactional
    @Override
    Account save(Account entity);
}
