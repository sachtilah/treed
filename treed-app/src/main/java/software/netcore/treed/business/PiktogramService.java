package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.PiktogramRepository;
import software.netcore.treed.data.schema.Piktogram;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class PiktogramService {

    private final PiktogramRepository piktogramRepository;

    public Iterable<Piktogram> getPics() {
        log.info("Getting all piktogram");
        return piktogramRepository.findAll();
    }

    public void savePic(Piktogram piktogram) {
        log.info("Saving new piktogram {}", piktogram);
        piktogramRepository.save(piktogram);
    }

    public void deletePic(Piktogram piktogram){
        log.info("Deleting selected piktogram {}", piktogram);
        piktogramRepository.delete(piktogram);
    }

}
