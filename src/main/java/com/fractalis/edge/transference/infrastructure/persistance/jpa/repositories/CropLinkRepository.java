package com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories;

import com.fractalis.edge.transference.domain.model.aggregates.CropLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CropLinkRepository extends JpaRepository<CropLink, Long> {
    boolean existsByCropCloudId(Long cropCloudId);
    Optional<CropLink> findByCropCloudId(Long cropCloudId);
}
