package com.app.benevole.model;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;

import com.app.benevole.enums.Status;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Recuperations")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Recuperation {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private LocalDateTime start;

    @Column
    private LocalDateTime end;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(columnDefinition = "longtext")
    private String rapport;

    @Column
    private LocalDateTime deleted;

    @ManyToMany
    @JoinTable(
            name = "RecuperationMagasins",
            joinColumns = @JoinColumn(name = "recuperationId"),
            inverseJoinColumns = @JoinColumn(name = "magasinId")
    )
    private Set<Magasin> magasins;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", unique = true)
    private Team team;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
