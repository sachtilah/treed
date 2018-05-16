package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.sim.SentenceRepository;
import software.netcore.treed.data.schema.sim.Sentence;

import javax.transaction.Transactional;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    public Iterable<Sentence> getSentences() {
        log.info("Getting all sentences");
        return sentenceRepository.findAll();
    }

    public void saveSentence(Sentence sentence) {
        log.info("Saving new sentence {}", sentence);
        sentenceRepository.save(sentence);
    }

    public void deleteSentence(Sentence sentence){
        log.info("Deleting selected sentence {}", sentence);
        sentenceRepository.delete(sentence);
    }

}

