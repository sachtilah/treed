package software.netcore.treed.data.schema;

import lombok.*;
import software.netcore.treed.data.converter.StringToHashConverter;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @since v. 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account")
@Entity
    public class Account extends AbstractEntity {

    /**
     * Unique username in the system.
     */
    @Column(nullable = false, unique = true)
    public String username;

    @Column(name = "password", nullable = false)
    @Convert(converter = StringToHashConverter.class)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column(name = "usermail")
    private String userMail;
}
