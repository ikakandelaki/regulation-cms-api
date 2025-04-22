package com.kandex.regulation.cms.repository;

import com.kandex.regulation.cms.model.entity.RegulationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegulationTypeRepository extends JpaRepository<RegulationTypeEntity, String> {
}
