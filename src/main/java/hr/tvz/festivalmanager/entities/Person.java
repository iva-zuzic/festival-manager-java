package hr.tvz.festivalmanager.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja apstraktnu osobu u aplikaciji za upravljanje festivalima.
 * Klasa sadrži zajedničke podatke za sve osobe u aplikaciji,
 * kao što su ime, prezime, email adresa i datum rođenja.
 * Konkretne podklase određuju vrstu osobe implementacijom metode
 * {@link #getType()}.
 */
public abstract class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected LocalDate dateOfBirth;

    protected Person(String firstName, String lastName, String email, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
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

    /**
     * Vraća naziv vrste osobe.
     * Svaka podklasa određuje vlastiti tekstualni opis vrste osobe,
     * primjerice član benda ili radnik.
     *
     * @return tekstualni opis vrste osobe
     */
    public abstract String getType();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(email, person.email) && Objects.equals(dateOfBirth, person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, dateOfBirth);
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "ime: " + firstName + '\'' +
                ", prezime: " + lastName + '\'' +
                ", email: " + email + '\'' +
                ", datum rođenja: " + dateOfBirth +
                '}';
    }
}
