package com.raphazrz.client_management_api.enumerator;

public enum ContactType {
    PHONE(1),
    EMAIL(2);

    private final int type;

    ContactType(int type) {
        this.type = type;
    }

    public static ContactType fromType(int type) {
        for (ContactType contact : ContactType.values()) {
            if (contact.getType() == type)
                return contact;
        }

        throw new InvalidContactTypeException("Invalid contact type. Valid values are: 1 (PHONE) and 2 (EMAIL).");
    }

    public int getType() {
        return type;
    }
}
