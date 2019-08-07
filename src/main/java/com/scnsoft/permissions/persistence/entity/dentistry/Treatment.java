package com.scnsoft.permissions.persistence.entity.dentistry;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.util.LocalDateConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "treatments")
public class Treatment implements PersistenceEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LocalDateConverter.class)
    @Column
    private LocalDate date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tooth_id")
    private UserTooth userTooth;

    @NotNull
    private String description;

    private BigDecimal cost;
}