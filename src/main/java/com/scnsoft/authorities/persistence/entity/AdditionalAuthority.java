package com.scnsoft.authorities.persistence.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data

public class AdditionalAuthority implements PersistenceEntity<Long>, Serializable {
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "auth_id")
    private Long authorityId;
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdditionalAuthority that = (AdditionalAuthority) o;
        return enabled == that.enabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(authorityId, that.authorityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, authorityId, enabled);
    }
}
