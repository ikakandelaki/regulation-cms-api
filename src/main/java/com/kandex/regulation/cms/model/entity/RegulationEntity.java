package com.kandex.regulation.cms.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "regulations")
@Getter
@Setter
public class RegulationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regulation_seq_gen")
    @SequenceGenerator(name = "regulation_seq_gen", sequenceName = "regulations_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "r_name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "sort")
    private Long sort;

    @Column(name = "test")
    private Integer test;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "regulation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RegulationContentEntity> contents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "regulation_relationships",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private Set<RegulationEntity> parents;

    @ManyToMany(mappedBy = "parents", fetch = FetchType.LAZY)
    private Set<RegulationEntity> children;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<RegulationRelationshipEntity> relationships;
}
