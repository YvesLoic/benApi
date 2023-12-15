package com.app.benevole.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
@Table(name = "Distributions")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Distribution {

    @ToString.Include
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column
    private LocalDateTime startDate;

    @ToString.Include
    @Column
    private LocalDateTime endDate;

    @ToString.Include
    @Column
    private LocalDateTime start;

    @ToString.Include
    @Column
    private LocalDateTime end;

    @ToString.Include
    @Column
    private String location;

    @ToString.Include
    @Column(columnDefinition = "longtext")
    private String rapport;

    @ToString.Include
    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ToString.Include
    @Column
    private LocalDateTime deleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", unique = true)
    private Team team;

    @ToString.Include
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @ToString.Include
    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
