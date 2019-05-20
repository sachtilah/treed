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
    @Column(name = "bytesImage", nullable = false)
    private byte[] bytesImage;

    @Lob
    @Column(name = "bytesAudio", length = 5 * 1024 * 1024)
    private byte[] bytesAudio;

    @Column(name = "term", nullable = false)
    private String term;

    @Column(name = "term_en")
    private String term_en;

    @Column(name = "term_ro")
    private String term_ro;

    @Column(name = "term_ru")
    private String term_ru;

}
