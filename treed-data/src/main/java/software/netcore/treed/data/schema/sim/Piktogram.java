package software.netcore.treed.data.schema.sim;

import lombok.Getter;
import lombok.Setter;
import software.netcore.treed.data.schema.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @since v.1.7.0
 */
@Getter
@Setter
@Table(name = "piktogram")
@Entity
public class Piktogram extends AbstractEntity {

    @Lob
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    @Column(name = "term", nullable = false)
    private String term;

}
