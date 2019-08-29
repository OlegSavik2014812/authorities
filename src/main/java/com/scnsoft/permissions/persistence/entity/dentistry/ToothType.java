package com.scnsoft.permissions.persistence.entity.dentistry;

public enum ToothType {
    INCISOR,
    CANINE,
    PREMOLAR,
    MOLAR;

    @Override
    public String toString() {
        return this.name();
    }
}
