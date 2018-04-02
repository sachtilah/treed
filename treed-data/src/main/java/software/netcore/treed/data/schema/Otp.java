package software.netcore.treed.data.schema;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @since v. 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "otp")
@Entity
public class Otp extends AbstractEntity {

    /**
     * Unique password in the system.
     */
    @Column(name="usermail", nullable = false, unique = true)
    public String usermail;

    @Column(name = "otpPass", nullable = false)
//    @Convert(converter = StringToHashConverter.class)
    private String otpPass;

}
