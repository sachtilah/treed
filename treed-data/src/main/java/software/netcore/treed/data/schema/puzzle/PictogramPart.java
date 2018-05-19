package software.netcore.treed.data.schema.puzzle;

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
@Table(name = "pictogramPart")
@Entity
public class PictogramPart extends AbstractEntity {

    @Lob
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    @Column(name = "pictPart", nullable = false)
    private String pictPart;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

}

