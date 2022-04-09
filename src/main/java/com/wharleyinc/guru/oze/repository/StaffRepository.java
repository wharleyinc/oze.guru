package com.wharleyinc.guru.oze.repository;

import com.wharleyinc.guru.oze.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the {@link Staff} entity.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findOneByUuid(UUID staffID);
}
