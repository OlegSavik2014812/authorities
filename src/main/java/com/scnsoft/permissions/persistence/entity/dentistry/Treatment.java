package com.scnsoft.permissions.persistence.entity.dentistry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "treatments")
public class Treatment extends BaseDentalRequest {
    private BigDecimal cost;
}