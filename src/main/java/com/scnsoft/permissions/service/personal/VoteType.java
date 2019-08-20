package com.scnsoft.permissions.service.personal;

public enum VoteType {
    FOR,
    AGAINST;

    public static VoteType fromString(String s) {
        if (s.equalsIgnoreCase(FOR.toString())) {
            return FOR;
        }
        if (s.equalsIgnoreCase(AGAINST.toString())) {
            return AGAINST;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}