package com.scnsoft.permissions.persistence.entity.dentistry;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "user_teeth")
public class UserTooth implements PersistenceEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Tooth tooth;

    @OneToMany(mappedBy = "userTooth",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Treatment> treatments;

    @OneToMany(mappedBy = "userTooth",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Complaint> complaints;
}
