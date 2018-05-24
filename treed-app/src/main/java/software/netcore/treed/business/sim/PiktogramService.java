package software.netcore.treed.business.sim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import software.netcore.treed.data.repository.sim.PiktogramRepository;
import software.netcore.treed.data.schema.sim.Piktogram;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class PiktogramService extends WebMvcConfigurerAdapter {

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

