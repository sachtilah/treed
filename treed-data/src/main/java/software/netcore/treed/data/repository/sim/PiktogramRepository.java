package software.netcore.treed.data.repository.sim;

import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.sim.Piktogram;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface PiktogramRepository extends BaseRepository<Piktogram> {

    Optional<Piktogram> findByTerm(String term);

}
