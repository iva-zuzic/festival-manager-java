package hr.tvz.festivalmanager.entities;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja radnika na festivalu.
 * Klasa nasljeđuje apstraktnu klasu {@link Person} i implementira sučelje
 * {@link Payable}, što znači da radnik ima podatak o honoraru.
 * Radnik ima određenu ulogu na festivalu, primjerice menadžer, tehničar,
 * zaštitar, volonter ili voditelj pozornice.
 */
public non-sealed class Worker extends Person implements Payable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Role {
        MENADZER,
        TONSKI_TEHNICAR,
        SVJETLOSNI_TEHNICAR,
        CISTAC,
        VOLONTER,
        ZASTITAR,
        VODITELJ_POZORNICE,
        TEHNICAR,
        OSTALO
    }

    private final Role role;
    private BigDecimal fee;

    public Worker(String firstName, String lastName, String email,
                  LocalDate dateOfBirth, Role role, BigDecimal fee) {
        super(firstName, lastName, email, dateOfBirth);
        this.role = role;
        this.fee = fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getType() {
        return "Radnik/ca na festivalu";
    }

    @Override
    public BigDecimal getFee() {
        return fee;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Worker worker = (Worker) o;
        return role == worker.role && Objects.equals(fee, worker.fee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role, fee);
    }

    @Override
    public String toString() {
        return "Radnik/ca{" +
                "ime: " + firstName +
                ", prezime: " + lastName +
                ", uloga: " + role +
                ", honorar: " + fee +
                '}';
    }
}
