package hr.tvz.festivalmanager.repository.files.json.dto;

import hr.tvz.festivalmanager.entities.Member;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.time.LocalDate;

public class MemberJson {

    private String firstName;
    private String lastName;
    private String email;

    @JsonbDateFormat("dd.MM.yyyy.")
    private LocalDate dateOfBirth;

    private Member.Role role;

    public MemberJson() {
    }

    public MemberJson(String firstName, String lastName, String email,
                      LocalDate dateOfBirth, Member.Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
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

    public Member.Role getRole() {
        return role;
    }

    public void setRole(Member.Role role) {
        this.role = role;
    }
}