package com.kandex.regulation.cms.repository;

import com.kandex.regulation.cms.model.entity.RegulationRelationshipEntity;
import com.kandex.regulation.cms.model.entity.RegulationRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegulationRelationshipRepository extends JpaRepository<RegulationRelationshipEntity, RegulationRelationshipId> {
    @Query("""
                SELECT COALESCE(MAX(rr.sort), 0) + 1
                FROM RegulationRelationshipEntity rr
                WHERE rr.parent.id = :parentId
            """)
    Long getNextSortForParent(Long parentId);
}
