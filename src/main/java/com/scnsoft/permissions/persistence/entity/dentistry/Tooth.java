package com.scnsoft.permissions.persistence.entity.dentistry;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "teeth")
public class Tooth implements PersistenceEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Enumerated(EnumType.STRING)
    private ToothType type;

    @OneToMany(mappedBy = "tooth",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<UserTooth> userTeeth;
}
