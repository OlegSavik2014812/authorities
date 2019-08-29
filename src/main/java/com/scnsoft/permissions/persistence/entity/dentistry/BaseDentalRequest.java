package com.scnsoft.permissions.persistence.entity.dentistry;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.util.LocalDateTimeConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseDentalRequest implements PersistenceEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = LocalDateTimeConverter.class)
    @Column
    private LocalDateTime date;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tooth_id")
    private UserTooth userTooth;
    @NotNull
    private String description;
}