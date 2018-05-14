package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.StoryRepository;
import software.netcore.treed.data.schema.sim.Sentence;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;

    public Iterable<Sentence> getStories() {
        log.info("Getting all stories");
        return storyRepository.findAll();
    }

    public void saveStory(Sentence story) {
        log.info("Saving new story {}", story);
        storyRepository.save(story);
    }

    public void deleteStory(Sentence story){
        log.info("Deleting selected story {}", story);
        storyRepository.delete(story);
    }

}

