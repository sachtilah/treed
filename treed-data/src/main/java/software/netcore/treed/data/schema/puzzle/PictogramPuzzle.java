package software.netcore.treed.data.schema.puzzle;

import lombok.Getter;
import lombok.Setter;
import software.netcore.treed.data.schema.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
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
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    @Column(name = "pictPuzzle", nullable = false)
    private String pictPuzzle;

    @Column(name = "components", nullable = false)
    private String[][] components;
}
