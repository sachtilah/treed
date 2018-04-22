package software.netcore.treed.data;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.schema.AbstractEntity;

import java.time.Instant;

/**
 * Base repository interface. Serve as a Every repo interface MUST extends this.
 *
 * @since v. 1.0.0
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseRepository<T extends AbstractEntity> extends PagingAndSortingRepository<T, Long> {


}
