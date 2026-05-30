package hr.tvz.festivalmanager.repository.console;

import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.StageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hr.tvz.festivalmanager.util.ConsoleUtils.*;

/**
 * Repozitorij za unos i dohvat pozornica putem konzole.
 * Klasa omogućuje korisniku unos naziva i kapaciteta pozornice.
 * Dodatno provjerava da se naziv iste pozornice ne unese više puta.
 */
public class ConsoleStageRepository implements StageRepository {
    private static final Logger log = LoggerFactory.getLogger(ConsoleStageRepository.class);
    private final List<Stage> stages = new ArrayList<>();
    private final Set<String> existingStageNames = new HashSet<>();
    private static final int NUMBER_OF_ENTITIES_TO_INPUT = 3;

    /**
     * Učitava podatke o pozornicama putem konzole.
     * Za svaku pozornicu korisnik unosi jedinstveni naziv i kapacitet.
     * Ako korisnik unese naziv pozornice koji već postoji, unos se ponavlja.
     */
    @Override
    public void inputAllEntities() {
        for (int i = 0; i < NUMBER_OF_ENTITIES_TO_INPUT; i++) {
            log.trace("Unos {}. pozornice", i + 1);

            String name;
            boolean nameValid;
            do {
                name = readString("Unesite ime pozornice: ");
                nameValid = existingStageNames.add(name);
                if (!nameValid) {
                    log.warn("Pozornica {} već postoji! Unesite drugo ime.", name);
                }
            } while (!nameValid);

            int capacity = readStrictlyPositiveInt("Unesite kapacitet pozornice: ");

            stages.add(new Stage(name, capacity));

            log.debug("Unesena pozornica: {}", name);
        }
        log.info("Unesene su sve pozornice");
    }

    /**
     * Vraća popis svih unesenih pozornica.
     * @return popis pozornica
     */
    @Override
    public List<Stage> getAllEntities() {
        return stages;
    }
}