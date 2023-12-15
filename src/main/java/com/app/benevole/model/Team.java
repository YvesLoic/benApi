package com.app.benevole.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;

import com.app.benevole.enums.TeamType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Teams")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Team {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private TeamType type;

    @Column
    private LocalDateTime deleted;

    @ManyToMany(mappedBy = "teams")
    private Set<User> members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_id")
    private Category categoryId;

    @OneToOne(mappedBy = "team", fetch = FetchType.LAZY)
    private Recuperation recuperation;

    @OneToOne(mappedBy = "team", fetch = FetchType.LAZY)
    private Distribution distribution;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
