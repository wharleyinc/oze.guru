package com.wharleyinc.guru.oze.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A StaffResponse.
 */
@Entity
@Table(name = "oze_patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

//    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private Integer age;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Patient(Long id, String name, Integer age, LocalDate lastVisitDate, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.lastVisitDate = lastVisitDate;
        this.updatedAt = updatedAt;
    }

    public Patient() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(LocalDate lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        setUpdatedAt(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PatientResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", lastVisitDate=" + lastVisitDate +
                ", updatedAt=" + updatedAt +
                '}';
    }


}
