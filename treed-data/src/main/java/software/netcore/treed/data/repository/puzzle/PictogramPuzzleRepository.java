package software.netcore.treed.data.repository.puzzle;

import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;

import java.util.Optional;

public interface PictogramPuzzleRepository extends BaseRepository<PictogramPuzzle> {

    Optional<PictogramPuzzle> findByPictPuzzle(String pictPuzzle);
}
