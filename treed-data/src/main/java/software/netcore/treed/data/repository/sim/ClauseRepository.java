package software.netcore.treed.data.repository.sim;


import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.sim.Clause;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface ClauseRepository extends BaseRepository<Clause> {

    Optional<Clause> findByName(String name);

}
