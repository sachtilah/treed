package software.netcore.treed.data;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Base repository interface. Serve as a Every repo interface MUST extends this.
 *
 * @since v. 1.4.0
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseRepository<T extends AbstractEntity> extends Repository<T, Long> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have
     * changed the entity instance completely. Set {@link AbstractEntity#createTime} to
     * {@link Instant#now()} for each new entity.
     *
     * @param entity entity
     * @return the saved entity
     */
    @Transactional
    <S extends T> S save(S entity);

}
