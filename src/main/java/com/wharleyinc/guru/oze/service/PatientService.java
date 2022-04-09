package com.wharleyinc.guru.oze.service;

import com.wharleyinc.guru.oze.domain.Patient;
import com.wharleyinc.guru.oze.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service class for managing patient.
 */
@Service
@Transactional
public class PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        }

    /**
     * Save a Patient.
     *
     * @param patient the entity to save.
     * @return the persisted entity.
     */
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);
        return patientRepository.save(patient);
    }

    /**
     * Get all Patients Profiles.
     *
     * @param years how many years past
     * @param pageable the pagination information.
     * @return the list of patients.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findAllPatientsByYearsPast(int years, Pageable pageable) {
        log.debug("Request to get all Patients by years past");

        LocalDate startDate = LocalDate.now().minusYears(years);
        return patientRepository.findAllByLastVisitDateGreaterThanEqualOrderByLastVisitDateDesc(startDate, pageable);
    }

    /**
     * Delete multiple patient profiles between date range.
     *
     * @param startDate the startDate of the date range.
     * @param endDate the endDate of the date range.
     */
    public void deletePatientsBetweenRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to delete patients' profiles between {}:  and  : {}", startDate, endDate);
        patientRepository.deletePatientsByLastVisitDateBetween(startDate, endDate);
    }
}
