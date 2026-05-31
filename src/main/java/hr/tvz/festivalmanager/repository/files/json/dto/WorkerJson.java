package hr.tvz.festivalmanager.repository.files.json.dto;

import hr.tvz.festivalmanager.entities.Worker;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WorkerJson {

    private String firstName;
    private String lastName;
    private String email;

    @JsonbDateFormat("dd.MM.yyyy.")
    private LocalDate dateOfBirth;

    private Worker.Role role;
    private BigDecimal fee;

    public WorkerJson() {
    }

    public WorkerJson(String firstName, String lastName, String email,
                      LocalDate dateOfBirth, Worker.Role role, BigDecimal fee) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.fee = fee;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Worker.Role getRole() {
        return role;
    }

    public void setRole(Worker.Role role) {
        this.role = role;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}