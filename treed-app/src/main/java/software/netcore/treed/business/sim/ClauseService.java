package software.netcore.treed.business.sim;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.sim.ClauseRepository;
import software.netcore.treed.data.schema.sim.Clause;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class ClauseService {

    private final ClauseRepository clauseRepository;

    public Iterable<Clause> getClauses() {
        log.info("Getting all clauses");
        return clauseRepository.findAll();
    }

    public void saveClause(Clause clause) {
        log.info("Saving new clause {}", clause);
        clauseRepository.save(clause);
    }

    public void deleteClause(Clause clause){
        log.info("Deleting selected clause {}", clause);
        clauseRepository.delete(clause);
    }

}

