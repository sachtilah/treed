package software.netcore.treed.puzzle;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import software.netcore.treed.data.repository.puzzle.PictogramPartRepository;
import software.netcore.treed.data.repository.puzzle.PictogramPuzzleRepository;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPuzzleService;

/**
 * @since v. 1.4.0
 */
@RequiredArgsConstructor
@Configuration
public class TreedPuzzleConfiguration extends WebMvcConfigurerAdapter {
   /* private final PictogramPartRepository pictogramPartRepo;
    private final PictogramPuzzleRepository pictogramPuzzleRepo;

    @Bean
    public PictogramPartService pictogramPartService() {
        return new PictogramPartService(pictogramPartRepo);
    }

    @Bean
    public PictogramPuzzleService pictogramPuzzleService() {
        return new PictogramPuzzleService(pictogramPuzzleRepo);
    }*/

}
