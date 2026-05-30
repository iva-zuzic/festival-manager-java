package hr.tvz.festivalmanager.repository.console;

import hr.tvz.festivalmanager.entities.Worker;
import hr.tvz.festivalmanager.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.tvz.festivalmanager.util.ConsoleUtils.*;

/**
 * Repozitorij za unos i dohvat radnika putem konzole.
 * Klasa omogućuje korisniku unos osnovnih podataka o radniku,
 * odabir njegove uloge na festivalu i unos honorara.
 */
public class ConsoleWorkerRepository implements WorkerRepository {
    private static final Logger log = LoggerFactory.getLogger(ConsoleWorkerRepository.class);
    private static final int NUMBER_OF_ENTITIES_TO_INPUT = 3;

    private final List<Worker> workers = new ArrayList<>();
    private final Set<String> existingEmails = new HashSet<>();

    /**
     * Učitava podatke o radnicima putem konzole.
     * Za svakog radnika korisnik unosi ime, prezime, email adresu,
     * datum rođenja, ulogu na festivalu i iznos honorara.
     */
    @Override
    public void inputAllEntities() {
        List<Worker.Role> roles = List.of(Worker.Role.values());

        for (int i = 0; i < NUMBER_OF_ENTITIES_TO_INPUT; i++) {
            log.trace("Unos {}. radnika", i + 1);

            String first = readString("Unesite ime: ");
            String last = readString("Unesite prezime: ");
            String email = readUniqueEmail("Unesite email: ", existingEmails);
            LocalDate date = readDate("Unesite datum rođenja: ");

            for (int j = 0; j < roles.size(); j++) {
                log.info("{}. {}", j + 1, roles.get(j));
            }
            int chosenRoleIdx = readIntInRange("Unesite redni broj uloge: ", 1, roles.size());
            Worker.Role chosenRole = roles.get(chosenRoleIdx - 1);

            BigDecimal fee = readNonNegativeBigDecimal("Unesite iznos honorara: ");

            workers.add(new Worker(first, last, email, date, chosenRole, fee));

            log.debug("Unesen radnik: {} {}", first, last);
        }
        log.info("Uneseni su svi radnici");
    }

    /**
     * Vraća popis svih unesenih radnika.
     * @return popis radnika
     */
    @Override
    public List<Worker> getAllEntities() {
        return workers;
    }
}