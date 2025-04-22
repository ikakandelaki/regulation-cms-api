package com.kandex.regulation.cms.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regulation_relationships")
@Getter
@Setter
public class RegulationRelationshipEntity {
    @EmbeddedId
    private RegulationRelationshipId id;

    @ManyToOne
    @MapsId("parentId")
    @JoinColumn(name = "parent_id")
    private RegulationEntity parent;

    @ManyToOne
    @MapsId("childId")
    @JoinColumn(name = "child_id")
    private RegulationEntity child;

    @Column(name = "sort")
    private Long sort;
}
