package software.netcore.treed.data.converter;

import javax.persistence.AttributeConverter;

/**
 * String to hash and vice versa converter. It use ...
 *
 * @since v. 1.4.0
 */
public class StringToHashConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}
