package software.netcore.treed.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Collection;
import java.util.Optional;

/**
 * @since v. 1.4.0
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    @Override
    Account findOne(Long aLong);

    @Override
    Collection<Account> findAll();

    @Override
    void delete(Long aLong);

    @Override
    void delete(Account entity);




    Optional<Account> findByUsername(String username);

}
