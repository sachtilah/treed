package software.netcore.treed.data;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import software.netcore.treed.data.schema.AbstractEntity;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Date;

/**
 * Used as a base implementation for all repository instances within spring context. Auto
 * assign {@link Instant#now()} to each new entity being save.
 *
 * @since v. 1.0.0
 */
public class BaseRepositoryImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Long>
        implements BaseRepository<T> {

    /**
     * Entity information
     */
    private final JpaEntityInformation<T, ?> entityInformation;

    /**
     * Entity manager
     */
    private final EntityManager em;

    /**
     * Default constructor
     *
     * @param entityInformation entity information
     * @param entityManager     entity manager
     */
    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        if (this.entityInformation.isNew(entity)) {
            entity.createTime = Date.from(Instant.now());
            this.em.persist(entity);
            return entity;
        } else {
            return this.em.merge(entity);
        }
    }

}