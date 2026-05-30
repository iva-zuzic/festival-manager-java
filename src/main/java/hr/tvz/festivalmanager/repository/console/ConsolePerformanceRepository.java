package hr.tvz.festivalmanager.repository.console;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Performance;
import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.ArtistRepository;
import hr.tvz.festivalmanager.repository.PerformanceRepository;
import hr.tvz.festivalmanager.repository.StageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static hr.tvz.festivalmanager.util.ConsoleUtils.*;

/**
 * Repozitorij za unos i dohvat izvedbi putem konzole.
 * Klasa omogućuje korisniku odabir umjetnika i pozornice iz postojećih
 * repozitorija te unos datuma, vremena početka i trajanja izvedbe.
 */
public class ConsolePerformanceRepository implements PerformanceRepository {
    private static final Logger log = LoggerFactory.getLogger(ConsolePerformanceRepository.class);
    private final List<Performance> performances = new ArrayList<>();
    private final ArtistRepository artistRepository;
    private final StageRepository stageRepository;
    private static final int NUMBER_OF_ENTITIES_TO_INPUT = 3;

    public ConsolePerformanceRepository(ArtistRepository artistRepository, StageRepository stageRepository) {
        this.artistRepository = artistRepository;
        this.stageRepository = stageRepository;
    }

    /**
     * Učitava podatke o izvedbama putem konzole.
     * Za svaku izvedbu korisnik odabire umjetnika i pozornicu,
     * unosi datum i vrijeme početka izvedbe te trajanje izvedbe u minutama.
     * Nakon unosa kreira se novi objekt klase {@link Performance}.
     */
    @Override
    public void inputAllEntities() {
        List<Artist> allArtists = artistRepository.getAllEntities();
        List<Stage> allStages = stageRepository.getAllEntities();

        for (int i = 0; i < NUMBER_OF_ENTITIES_TO_INPUT; i++) {
            log.trace("Unos {}. izvedbe", i + 1);

            log.info("Dostupni umjetnici:");
            for (int j = 0; j < allArtists.size(); j++) {
                log.info("{}. {}", j + 1, allArtists.get(j).getStageName());
            }
            int idxArtist = readIntInRange("Unesite redni broj umjetnika: ", 1, allArtists.size());
            Artist chosenArtist = allArtists.get(idxArtist - 1);

            log.info("Dostupne pozornice:");
            for (int k = 0; k < allStages.size(); k++) {
                log.info("{}. {}", k + 1, allStages.get(k).name());
            }
            int idxStage = readIntInRange("Unesite redni broj pozornice: ", 1, allStages.size());
            Stage chosenStage = allStages.get(idxStage - 1);

            LocalDateTime start = readDateTime("Unesite datum i vrijeme izvedbe (dd.MM.yyyy. HH:mm): ");

            int dur = readStrictlyPositiveInt("Unesite trajanje u minutama: ");

            performances.add(new Performance(chosenArtist, chosenStage, start, dur));

            log.debug("Unesena izvedba: {} na {}", chosenArtist.getStageName(), chosenStage.name());
        }
        log.info("Unesene su sve izvedbe");
    }

    /**
     * Vraća popis svih unesenih izvedbi.
     * @return popis izvedbi
     */
    @Override
    public List<Performance> getAllEntities() {
        return performances;
    }
}