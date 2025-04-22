package com.kandex.regulation.cms.repository;

import com.kandex.regulation.cms.model.entity.RegulationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegulationRepository extends JpaRepository<RegulationEntity, Long> {

    Optional<RegulationEntity> findByName(String name);

    @Query("""
                SELECT r FROM RegulationEntity r
                WHERE r.type = 'sub'
                AND r.id NOT IN (
                    SELECT child.id FROM RegulationEntity main
                    JOIN main.children child
                    WHERE main.id = :mainRegulationId
                )
            """)
    List<RegulationEntity> findPossibleSubRegulations(Long mainRegulationId);

    @Query("SELECT r FROM RegulationEntity r LEFT JOIN FETCH r.relationships re LEFT JOIN FETCH re.child WHERE r.type=:name")
    List<RegulationEntity> findByTypeJoinFetchRelationships(String name);

    List<RegulationEntity> findByType(String name);

    @Query("SELECT r FROM RegulationEntity r LEFT JOIN FETCH r.relationships re LEFT JOIN FETCH re.child " +
            "LEFT JOIN FETCH r.contents")
    List<RegulationEntity> findAllJoinFetch();

    @Query("SELECT COALESCE(MAX(r.sort), 0) + 1 FROM RegulationEntity r WHERE r.type = 'main'")
    Long getNextSortForMain();

    Optional<RegulationEntity> findByIdAndType(Long id, String type);
}
