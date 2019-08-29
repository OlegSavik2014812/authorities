package com.scnsoft.permissions.persistence.entity.dentistry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "complaints")
public class Complaint extends BaseDentalRequest {
    public static Builder complain() {
        return new Complaint().new Builder();
    }

    public class Builder {
        private Builder() {
        }

        public Builder about(UserTooth userTooth) {
            Complaint.super.setUserTooth(userTooth);
            return this;
        }

        public Builder when(LocalDateTime localDateTime) {
            Complaint.super.setDate(localDateTime);
            return this;
        }

        public Builder describe(String description) {
            Complaint.super.setDescription(description);
            return this;
        }

        public Complaint build() {
            return Complaint.this;
        }
    }
}