package com.wharleyinc.guru.oze.web;

import com.wharleyinc.guru.oze.domain.Patient;
import com.wharleyinc.guru.oze.domain.Staff;
import com.wharleyinc.guru.oze.domain.apiRequest.DateRangeRequest;
import com.wharleyinc.guru.oze.service.CSVService;
import com.wharleyinc.guru.oze.service.PatientService;
import com.wharleyinc.guru.oze.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing staff.
*/
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private final StaffService staffService;
    private final PatientService patientService;
    private final CSVService csvService;


    public PatientResource(StaffService staffService, PatientService patientService, CSVService csvService) {
        this.staffService = staffService;
        this.patientService = patientService;
        this.csvService = csvService;
    }

    /**
     * {@code POST  /patient} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)} and with body the new patient, or with status {@code 400 (Bad Request)} if the status has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient/{id}")
    public ResponseEntity<Patient> createPatient(@PathVariable(value = "id", required = true) final String id, @Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);

        Optional<Staff> validatedStaff =  staffService.findStaffProfileByUuid(UUID.fromString(id));
        if (validatedStaff.isEmpty()) {
            throw new URISyntaxException(HttpStatus.NOT_FOUND.toString(), "No staff profile found with the given id");
        }

        if (patient.getId() != null) {
            throw new URISyntaxException(HttpStatus.BAD_REQUEST.toString(), "A new Patient cannot already have an ID");
        }
        return ResponseEntity.ok().body(patientService.save(patient));
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<Patient>> fetchPatientsByYearsPast(@PathVariable(value = "id", required = true) final String id, @RequestParam int yearsPast, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to fetch patients by no of years past: {}", id);

        Optional<Staff> validatedStaff =  staffService.findStaffProfileByUuid(UUID.fromString(id));
        if (validatedStaff.isEmpty()) {
            List<Patient> patientList = new ArrayList<>();
            return ResponseEntity.badRequest().body(patientList);
        }

        return ResponseEntity.ok(patientService.findAllPatientsByYearsPast(yearsPast, pageable).getContent());
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatientsBetweenDateRange(@PathVariable(value = "id", required = true) final String id, @RequestBody DateRangeRequest dateRangeRequest) {
        log.debug("REST request to delete patients between date range {}", dateRangeRequest);

        Optional<Staff> validatedStaff =  staffService.findStaffProfileByUuid(UUID.fromString(id));
        if (validatedStaff.isEmpty()) {
            return (ResponseEntity<Void>) ResponseEntity.badRequest();
        }

        LocalDate startDate = new java.sql.Date(dateRangeRequest.getStartDate().getTime()).toLocalDate();
        LocalDate endDate = new java.sql.Date(dateRangeRequest.getEndDate().getTime()).toLocalDate();

        patientService.deletePatientsBetweenRange(startDate, endDate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/download/{id}")
    public ResponseEntity<Resource> downloadPatientProfile(@PathVariable(value = "id", required = true) final String id, @RequestParam Long patientID) {
        log.debug("REST request to download patient's profile:  {}", patientID);
        Optional<Staff> validatedStaff =  staffService.findStaffProfileByUuid(UUID.fromString(id));
        if (validatedStaff.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String filename = "patient.csv";
        InputStreamResource file = new InputStreamResource(csvService.load(patientID));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}

