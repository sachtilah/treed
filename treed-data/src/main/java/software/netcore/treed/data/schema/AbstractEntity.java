package software.netcore.treed.data.schema;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

/**
 * @since v. 1.0.0
 */
@Getter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEntity implements Serializable{

    @Id
    @GeneratedValue
    public Long id;

    @Setter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime;

}
