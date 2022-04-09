package com.wharleyinc.guru.oze.repository;

import com.wharleyinc.guru.oze.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Spring Data JPA repository for the {@link Patient} entity.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAllByLastVisitDateGreaterThanEqualOrderByLastVisitDateDesc(LocalDate startDate, Pageable pageable);
    void deletePatientsByLastVisitDateBetween(LocalDate startDate, LocalDate endDate);
}
