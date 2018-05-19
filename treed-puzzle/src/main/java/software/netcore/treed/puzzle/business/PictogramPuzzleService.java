package software.netcore.treed.puzzle.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.puzzle.PictogramPuzzleRepository;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;

@RequiredArgsConstructor
@Slf4j
public class PictogramPuzzleService {
    private final PictogramPuzzleRepository pictogramPuzzleRepository;

    public Iterable<PictogramPuzzle> getPics() {
        log.info("Getting all pictogram");
        return pictogramPuzzleRepository.findAll();
    }

    public void savePic(PictogramPuzzle pictogramPuzzle) {
        log.info("Saving new pictogram {}", pictogramPuzzle);
        pictogramPuzzleRepository.save(pictogramPuzzle);
    }

    public void deletePic(PictogramPuzzle pictogramPuzzle){
        log.info("Deleting selected pictogram {}", pictogramPuzzle);
        pictogramPuzzleRepository.delete(pictogramPuzzle);
    }
}
