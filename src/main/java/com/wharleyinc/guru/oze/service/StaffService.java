package com.wharleyinc.guru.oze.service;

import com.wharleyinc.guru.oze.domain.Patient;
import com.wharleyinc.guru.oze.domain.Staff;
import com.wharleyinc.guru.oze.repository.StaffRepository;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Service class for managing staff.
 */
@Service
@Transactional
public class StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffService.class);

    private final StaffRepository staffRepository;
    private final PatientService patientService;

    public StaffService(StaffRepository staffRepository, PatientService patientService) {
        this.staffRepository = staffRepository;
        this.patientService = patientService;
    }

    /**
     * Save a Staff Profile.
     *
     * @param staff the entity to save.
     * @return the persisted entity.
     */
    public Staff save(Staff staff) {
        log.debug("Request to save Staff Profile: {}", staff);
        return staffRepository.save(staff);
    }

    @Transactional()
    public Optional<Staff> updateStaffProfile(Staff staff) {
        log.debug("Request to update Staff Profile : {}", staff);
            return staffRepository
                    .findById(staff.getId())
                    .map(existingStaff -> {
                        if (staff.getName() != null) {
                            existingStaff.setName(staff.getName());
                        }
                        if (staff.getRegistrationDate() != null) {
                            existingStaff.setRegistrationDate(staff.getRegistrationDate());
                        }

                        return existingStaff;
                    })
                    .map(staffRepository::save);
    }

    @Transactional(readOnly = true)
    public Optional<Staff> findStaffProfileByUuid(UUID staffID) {
        log.debug("Request to find staff profile by uuid : {}", staffID);

        return staffRepository.findOneByUuid(staffID);
    }

    @Transactional()
    public void generateInitialData() {
        log.debug("Request to find staff profile by uuid : {}");

        for (int i = 0; i < 150; i++) {
            Staff staff = new Staff();
            String sFirstName = generateRandomString(6);
            String sLastName = generateRandomString(5);
            staff.setName(sFirstName + " " + sLastName);
            LocalDate registrationDate = LocalDate.now();
            staff.setRegistrationDate(registrationDate.minusWeeks(generateRandomNumber()));

            staffRepository.save(staff);

            Patient patient = new Patient();
            String pFirstName = generateRandomString(5);
            String pLastName = generateRandomString(6);
            patient.setName(pFirstName + " " + pLastName);
            patient.setAge(generateRandomNumber());
            patient.setLastVisitDate(LocalDate.now().minusMonths(generateRandomNumber()));

            patientService.save(patient);
        }
    }

    public String generateRandomString(int x) {
        return RandomStringUtils.randomAlphabetic(x);
    }

    public int generateRandomNumber() {
        Random r = new Random();
        int low = 1;
        int high = 100;
        return r.nextInt(high - low) + low;
    }
}
