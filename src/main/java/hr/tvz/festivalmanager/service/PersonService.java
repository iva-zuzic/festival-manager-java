package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Payable;
import hr.tvz.festivalmanager.entities.Person;
import hr.tvz.festivalmanager.entities.Worker;
import hr.tvz.festivalmanager.exception.EntityNotFoundException;

import java.util.Comparator;
import java.util.List;

/**
 * Servisna klasa za rad s osobama.
 */
public class PersonService {
    private static final String EMPTY_LIST_MESSAGE = "Prazna lista!";

    private PersonService() {
    }

    /**
     * Pronalazi najmlađu osobu.
     *
     * @param persons lista osoba
     * @return najmlađa osoba
     */
    public static Person findYoungest(List<? extends Person> persons) {
        return persons.stream()
                .max(Comparator.comparing(Person::getDateOfBirth))
                .orElseThrow(() -> new EntityNotFoundException(EMPTY_LIST_MESSAGE));
    }

    /**
     * Pronalazi najstariju osobu.
     *
     * @param persons lista osoba
     * @return najstarija osoba
     */
    public static Person findOldest(List<? extends Person> persons) {
        return persons.stream()
                .min(Comparator.comparing(Person::getDateOfBirth))
                .orElseThrow(() -> new EntityNotFoundException(EMPTY_LIST_MESSAGE));
    }

    /**
     * Pronalazi osobu s najvećim honorarom.
     *
     * @param list lista osoba koje imaju honorar
     * @param <T> tip osobe koja nasljeđuje {@link Person} i implementira {@link Payable}
     * @return osoba s najvećim honorarom
     */
    public static <T extends Person & Payable> T findHighestPaid(List<T> list) {
        return list.stream()
                .max(Comparator.comparing(Payable::getFee))
                .orElseThrow(() -> new EntityNotFoundException(EMPTY_LIST_MESSAGE));
    }

    /**
     * Pronalazi osobu s najmanjim honorarom.
     *
     * @param list lista osoba koje imaju honorar
     * @param <T> tip osobe koja nasljeđuje {@link Person} i implementira {@link Payable}
     * @return osoba s najmanjim honorarom
     */
    public static <T extends Person & Payable> T findLowestPaid(List<T> list) {
        return list.stream()
                .min(Comparator.comparing(Payable::getFee))
                .orElseThrow(() -> new EntityNotFoundException(EMPTY_LIST_MESSAGE));
    }

    /**
     * Dodaje osobe iz jedne liste u drugu listu.
     *
     * @param source lista iz koje se osobe čitaju
     * @param destination lista u koju se osobe dodaju
     * @param <T> tip osobe
     */
    public static <T extends Person> void addPersonsToList(List<T> source, List<? super T> destination) {
        destination.addAll(source);
    }
}
