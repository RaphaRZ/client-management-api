package com.raphazrz.client_management_api.converter;

import com.raphazrz.client_management_api.enumerator.ContactType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ContactTypeConverter implements Converter<Integer, ContactType> {
    @Override
    public ContactType convert(Integer source) {
        return ContactType.fromType(source);
    }
}
