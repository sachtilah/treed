package software.netcore.treed.data.repository.sim;


import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.sim.Sentence;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface SentenceRepository extends BaseRepository<Sentence> {

    Optional<Sentence> findByName(String name);

}
