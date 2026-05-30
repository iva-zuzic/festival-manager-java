package hr.tvz.festivalmanager.repository.console;

import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.tvz.festivalmanager.util.ConsoleUtils.*;

/**
 * Repozitorij za unos i dohvat članova benda putem konzole.
 * Klasa omogućuje korisniku unos osnovnih podataka o članu,
 * provjeru jedinstvenosti email adrese te odabir uloge člana.
 */
public class ConsoleMemberRepository implements MemberRepository {
    private static final Logger log = LoggerFactory.getLogger(ConsoleMemberRepository.class);
    private final List<Member> members = new ArrayList<>();
    private final Set<String> existingEmails = new HashSet<>();
    private static final int NUMBER_OF_ENTITIES_TO_INPUT = 3;

    /**
     * Učitava podatke o članovima benda putem konzole.
     * Za svakog člana korisnik unosi ime, prezime, email adresu,
     * datum rođenja i ulogu u bendu.
     * Metoda dodatno provjerava da se ista email adresa ne unese više puta.
     */
    @Override
    public void inputAllEntities() {
        List<Member.Role> roles = List.of(Member.Role.values());

        for (int i = 0; i < NUMBER_OF_ENTITIES_TO_INPUT; i++) {
            log.trace("Unos {}. člana", i + 1);

            String first = readString("Unesite ime: ");
            String last = readString("Unesite prezime: ");

            String email = readUniqueEmail("Unesite email: ", existingEmails);

            LocalDate date = readDate("Unesite datum rođenja: ");

            for (int j = 0; j < roles.size(); j++) {
                log.info("{}. {}", j + 1, roles.get(j));
            }
            int chosenRoleIdx = readIntInRange("Unesite redni broj uloge: ", 1, roles.size());
            Member.Role chosenRole = roles.get(chosenRoleIdx - 1);

            members.add(new Member(first, last, email, date, chosenRole));

            log.debug("Uneseni član: {} {}", first, last);
        }
        log.info("Uneseni su svi članovi");
    }

    /**
     * Vraća popis svih unesenih članova benda.
     * @return popis članova benda
     */
    @Override
    public List<Member> getAllEntities() {
        return members;
    }
}