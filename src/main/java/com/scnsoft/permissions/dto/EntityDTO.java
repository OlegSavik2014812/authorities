package com.scnsoft.permissions.dto;

import java.io.Serializable;

public interface EntityDTO<T> extends Serializable {
    T getId();
}