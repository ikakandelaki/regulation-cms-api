package com.kandex.regulation.cms.repository;

import com.kandex.regulation.cms.model.entity.RegulationContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationContentRepository extends JpaRepository<RegulationContentEntity, Long> {
}
