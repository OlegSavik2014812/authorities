package com.scnsoft.permissions.persistence.entity.personal;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile implements PersistenceEntity<Long> {
    @Id
    private Long id;

    @NotNull
    @Size(max = 256)
    private String firstName;

    @NotNull
    @Size(max = 256)
    private String lastName;

    @NotNull
    @Size(max = 256)
    private String email;

    @NotNull
    @Size(max = 256)
    private String phoneNumber;

    @OneToOne
    @JoinColumn
    @MapsId
    private User user;
}