package software.netcore.treed.data.repository;

import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.Account;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface AccountRepository extends BaseRepository<Account> {

    Optional<Account> findByUsername(String username);

}
