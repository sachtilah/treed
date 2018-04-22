package software.netcore.treed.data.converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeConverter;

/**
 * String to hash converter. Delegates conversion to the underlying {@link BCryptPasswordEncoder}.
 *
 * @since v. 1.0.0
 */
public class StringToHashConverter implements AttributeConverter<String, String> {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncoder.encode(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}
