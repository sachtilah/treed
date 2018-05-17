package software.netcore.treed.data.repository.puzzle;

import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.puzzle.PictogramPart;

import java.util.Optional;

public interface PictogramPartRepository  extends BaseRepository<PictogramPart> {

    Optional<PictogramPart> findByPictPart(String pictPart);
}
