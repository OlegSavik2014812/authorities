package com.scnsoft.permissions.persistence.entity.permission;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_permissions")
public class AdditionalPermission implements PersistenceEntity<CompositePermissionId> {
    @EmbeddedId
    private CompositePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permission_id")
    private Permission permission;

    @Column(name = "enabled", columnDefinition = "TINYINT", length = 1)
    private boolean isEnabled;
}