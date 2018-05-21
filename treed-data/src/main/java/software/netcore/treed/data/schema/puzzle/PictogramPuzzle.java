package software.netcore.treed.data.schema.puzzle;

import lombok.Getter;
import lombok.Setter;
import software.netcore.treed.data.schema.AbstractEntity;
//import software.netcore.treed.data.schema.sim.Piktogram;

import javax.persistence.*;
import java.util.Collection;
//import java.util.List;

/**
 * @since v.1.7.0
 */
@Getter
@Setter
@Table(name = "pictogramPuzzle")
@Entity
public class PictogramPuzzle extends AbstractEntity {

    @Lob
    /*@Column(name = "bytes", nullable = false)
    private byte[] bytes;*/

    @Column(name = "pictPuzzle", nullable = false)
    private String pictPuzzle;

    @Column(name = "components", nullable = false)
    private String[][] components;

    /*@OneToMany(fetch = FetchType.EAGER)
    private Collection<String[]> position;*/
}
