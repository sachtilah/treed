package software.netcore.treed.data.schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

/**
 * @since v.1.0.0
 */
@Getter
@Setter
@ToString
@Table(name = "sentence")
@Entity
public class Sentence extends AbstractEntity {

    @Column(name = "row_count", nullable = false)
    private Integer rowCount;

    @Column(name = "column_count", nullable = false)
    private Integer columnCount;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Piktogram> piktograms;

}
