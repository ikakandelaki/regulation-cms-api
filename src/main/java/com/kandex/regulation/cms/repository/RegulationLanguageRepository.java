package com.kandex.regulation.cms.repository;

import com.kandex.regulation.cms.model.entity.RegulationLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface RegulationLanguageRepository extends JpaRepository<RegulationLanguageEntity, String> {
    Optional<RegulationLanguageEntity> findByName(String name);

    @Query("SELECT rl.code FROM RegulationLanguageEntity rl WHERE rl.code IN :languageCodes")
    Set<String> findLanguagesByCodes(Set<String> languageCodes);
}
