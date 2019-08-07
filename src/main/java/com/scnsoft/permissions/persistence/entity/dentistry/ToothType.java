package com.scnsoft.permissions.persistence.entity.dentistry;

public enum ToothType {
    INCISOR,
    CANINE,
    PREMOLAR,
    MOLAR;

    public static ToothType fromString(String toothType) {
        for (ToothType type : values()) {
            if (type.name().equalsIgnoreCase(toothType)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
