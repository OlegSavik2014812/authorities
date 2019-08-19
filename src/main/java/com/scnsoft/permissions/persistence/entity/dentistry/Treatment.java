package com.scnsoft.permissions.persistence.entity.dentistry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "treatments")
public class Treatment extends BaseDentalRequest {
    private BigDecimal price;

    public static Builder treat() {
        return new Treatment().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder what(UserTooth userTooth) {
            Treatment.super.setUserTooth(userTooth);
            return this;
        }

        public Builder when(LocalDate localDate) {
            Treatment.super.setDate(localDate);
            return this;
        }

        public Builder describe(String description) {
            Treatment.super.setDescription(description);
            return this;
        }

        public Builder calculate(BigDecimal cost) {
            Treatment.this.price = cost;
            return this;
        }

        public Treatment build() {
            return Treatment.this;
        }
    }
}