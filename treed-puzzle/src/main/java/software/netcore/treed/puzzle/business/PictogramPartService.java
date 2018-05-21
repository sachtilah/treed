package software.netcore.treed.puzzle.business;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.puzzle.PictogramPartRepository;
import software.netcore.treed.data.schema.puzzle.PictogramPart;

@RequiredArgsConstructor
@Slf4j
public class PictogramPartService {

    private final PictogramPartRepository pictogramPartRepository;

    public Iterable<PictogramPart> getPics() {
        log.info("Getting all part of pictogram");
        return pictogramPartRepository.findAll();
    }

    public void savePic(PictogramPart pictogramPart) {
        log.info("Saving new pictogram {}", pictogramPart);
        pictogramPartRepository.save(pictogramPart);
    }

    public void deletePic(PictogramPart pictogramPart){
        log.info("Deleting selected pictogram {}", pictogramPart);
        pictogramPartRepository.delete(pictogramPart);
    }
}
