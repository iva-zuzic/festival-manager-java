package hr.tvz.festivalmanager.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja člana benda ili izvođačke skupine na festivalu.
 * Klasa nasljeđuje apstraktnu klasu {@link Person} i dodaje podatak
 * o ulozi člana u bendu, primjerice vokalist, bubnjar, gitarist ili basist.
 */
public class Member extends Person {
    public enum Role {
        VOKALIST,
        BUBNJAR,
        GITARIST,
        BASIST,
        KLAVIJATURIST,
        OSTALO
    }

    private final Role role;

    public Member(String firstName, String lastName, String email, LocalDate dateOfBirth, Role role) {
        super(firstName, lastName, email, dateOfBirth);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getType() {
        return "Član/ica benda";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return role == member.role;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }

    @Override
    public String toString() {
        return "Član/ica{" +
                "ime: " + firstName +
                ", prezime: " + lastName +
                ", uloga: " + role +
                '}';
    }
}
