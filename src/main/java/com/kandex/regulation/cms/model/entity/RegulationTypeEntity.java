package com.kandex.regulation.cms.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regulation_types")
@Getter
@Setter
public class RegulationTypeEntity {
    @Id
    private String name;
}
