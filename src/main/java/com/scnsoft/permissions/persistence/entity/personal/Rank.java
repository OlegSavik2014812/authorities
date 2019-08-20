package com.scnsoft.permissions.persistence.entity.personal;

import com.scnsoft.permissions.persistence.entity.PersistenceEntity;
import com.scnsoft.permissions.persistence.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ranks")
public class Rank implements PersistenceEntity<Long> {
    @Id
    private Long id;
    private Long numberOfVotes;
    private Long statistic;
    @OneToOne
    @JoinColumn
    @MapsId
    private User user;
}