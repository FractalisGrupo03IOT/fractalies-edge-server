package com.fractalis.edge.transference.infrastructure.persistance.jpa.repositories;

import com.fractalis.edge.transference.domain.model.aggregates.FlowerpotLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowerpotLinkRepository extends JpaRepository<FlowerpotLink, Long> {
    boolean existsByFlowerpotCloudId(Long flowerpotCloudId);
    Optional<FlowerpotLink> findByFlowerpotCloudId(Long flowerpotCloudId);
}
