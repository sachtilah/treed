package software.netcore.treed.data.repository;


import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.sim.Sentence;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface StoryRepository extends BaseRepository<Sentence> {

    Optional<Sentence> findByName(String name);

}
