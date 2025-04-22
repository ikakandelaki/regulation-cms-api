package com.kandex.regulation.cms.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "regulation_contents")
@Getter
@Setter
public class RegulationContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regulation_content_seq_gen")
    @SequenceGenerator(name = "regulation_content_seq_gen", sequenceName = "regulation_contents_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "lang")
    private String lang;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String text;

    @ManyToOne
    @JoinColumn(name = "regulation_id")
    private RegulationEntity regulation;
}
