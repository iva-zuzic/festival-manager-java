package hr.tvz.festivalmanager.app;

import hr.tvz.festivalmanager.entities.*;
import hr.tvz.festivalmanager.repository.*;
import hr.tvz.festivalmanager.repository.console.*;
import hr.tvz.festivalmanager.repository.files.json.JsonMemberRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonStageRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonWorkerRepository;
import hr.tvz.festivalmanager.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static hr.tvz.festivalmanager.util.ConsoleUtils.readNonNegativeInt;
import static hr.tvz.festivalmanager.util.ConsoleUtils.readString;

/**
 * Glavna klasa aplikacije za upravljanje festivalima.
 * Klasa inicijalizira repozitorije i servise, pokreće unos podataka
 * te omogućuje korisniku rad s aplikacijom putem glavnog izbornika.
 * Korisnik može pretraživati, sortirati i grupirati podatke te
 * pronalaziti objekte s minimalnim i maksimalnim vrijednostima.
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /**
     * Sprječava instanciranje glavne klase aplikacije.
     */
    private Main() {
    }

    /**
     * Pokreće aplikaciju za upravljanje festivalima.
     * U slučaju neočekivane pogreške prekid rada aplikacije zapisuje se
     * pomoću {@code ERROR} razine logiranja.
     */
    static void main() {
        try {
            startApplication();
        } catch (RuntimeException exception) {
            log.error("Aplikacija je prekinuta zbog neočekivane pogreške.", exception);
        }
    }

    /**
     * Inicijalizira repozitorije i servise aplikacije, učitava podatke
     * koje korisnik unosi te pokreće obradu glavnog izbornika.
     * Članovi i radnici dodaju se u zajedničku listu osoba kako bi se
     * nad njima mogla primijeniti polimorfna obrada podataka.
     */
    private static void startApplication() {
        MemberRepository memberRepository = new JsonMemberRepository();
        WorkerRepository workerRepository = new JsonWorkerRepository();
        ArtistRepository artistRepository = new ConsoleArtistRepository(memberRepository);
        StageRepository stageRepository = new JsonStageRepository();
        PerformanceRepository performanceRepository = new ConsolePerformanceRepository(artistRepository, stageRepository);

        memberRepository.inputAllEntities();
        workerRepository.inputAllEntities();
        artistRepository.inputAllEntities();
        stageRepository.inputAllEntities();
        performanceRepository.inputAllEntities();

        MemberService memberService = new MemberService(memberRepository);
        WorkerService workerService = new WorkerService(workerRepository);
        ArtistService artistService = new ArtistService(artistRepository);
        StageService stageService = new StageService(stageRepository);
        PerformanceService performanceService = new PerformanceService(performanceRepository);

        List<Person> allPersons = new ArrayList<>();
        PersonService.addPersonsToList(
                memberRepository.getAllEntities(),
                allPersons
        );
        PersonService.addPersonsToList(
                workerRepository.getAllEntities(),
                allPersons
        );

        processMenuChoice(allPersons, memberService, workerService,
                artistService, stageService, performanceService, workerRepository);

    }

    /**
     * Prikazuje glavni izbornik i obrađuje korisnički odabir sve dok
     * korisnik ne odabere opciju za izlaz iz aplikacije.
     * Ovisno o odabranoj opciji, metoda poziva odgovarajuće servisne metode
     * za pronalaženje, sortiranje, grupiranje ili pretraživanje podataka.
     *
     * @param allPersons         lista svih osoba korištena za pronalaženje
     *                           najmlađe i najstarije osobe
     * @param memberService      servis za rad s članovima izvođača
     * @param workerService      servis za rad s radnicima
     * @param artistService      servis za rad s umjetnicima
     * @param stageService       servis za rad s pozornicama
     * @param performanceService servis za rad s izvedbama
     * @param workerRepository   repozitorij radnika korišten za dohvat
     *                           podataka o honorarima radnika
     */
    public static void processMenuChoice(List<Person> allPersons,
                                         MemberService memberService,
                                         WorkerService workerService,
                                         ArtistService artistService,
                                         StageService stageService,
                                         PerformanceService performanceService,
                                         WorkerRepository workerRepository) {
        int choice;

        do {
            showMenu();
            choice = readNonNegativeInt("Odaberite opciju: ");

            switch (choice) {
                case 1 -> log.info("Najmlađa osoba: {}", PersonService.findYoungest(allPersons));
                case 2 -> log.info("Najstarija osoba: {}", PersonService.findOldest(allPersons));
                case 3 -> log.info("Najplaćeniji umjetnik: {}", artistService.getHighestPaidArtist());
                case 4 -> log.info("Umjetnici sortirani po honoraru: {}", artistService.sortByFee());
                case 5 -> log.info("Izvedbe grupirane po pozornici: {}", performanceService.groupByStage());
                case 6 -> log.info("Članovi grupirani po ulozi: {}", memberService.groupByRole());
                case 7 -> displayWorkersByVolunteerStatus(workerRepository);
                case 8 -> log.info("Umjetnici grupirani po žanru: {}", artistService.groupByGenre());
                case 9 -> log.info("Najplaćeniji radnik: {}",
                        PersonService.findHighestPaid(workerRepository.getAllEntities()));
                case 10 -> log.info("Najmanje plaćeni umjetnik: {}", artistService.getLowestPaidArtist());
                case 11 -> log.info("Najmanje plaćeni radnik: {}",
                        PersonService.findLowestPaid(workerRepository.getAllEntities()));
                case 12 -> log.info("Pozornice sortirane po kapacitetu: {}", stageService.sortByCapacity());
                case 13 -> log.info("Pronađeni umjetnici: {}",
                        artistService.findByName(readString("Unesite ime umjetnika: ")));
                case 14 -> log.info("Pronađeni članovi: {}",
                        memberService.findByRole(readMemberRole()));
                case 15 -> log.info("Pronađene pozornice: {}",
                        stageService.findByName(readString("Unesite naziv pozornice: ")));
                case 16 -> log.info("Pronađene izvedbe: {}",
                        performanceService.findByArtistName(readString("Unesite ime umjetnika: ")));
                case 17 -> log.info("Scenska imena umjetnika: {}", artistService.getArtistStageNames());
                case 0 -> log.info("Doviđenja!");
                default -> log.warn("Nepoznata opcija: {}", choice);
            }

        } while (choice != 0);
    }

    /**
     * Prikazuje sve dostupne opcije glavnog izbornika aplikacije.
     */
    private static void showMenu() {
        log.info("=== IZBORNIK ===");
        log.info("1. Pronađi najmlađe osobu");
        log.info("2. Pronađi najstariju osobu");
        log.info("3. Pronađi najplaćenijeg umjetnika");
        log.info("4. Sortiraj umjetnike po honoraru");
        log.info("5. Grupiraj izvedbe po pozornici");
        log.info("6. Grupiraj članove po ulozi");
        log.info("7. Podijeli radnike na volontere i plaćene");
        log.info("8. Grupiraj umjetnike po žanru");
        log.info("9. Pronađi najplaćenijeg radnika");
        log.info("10. Pronađi najmanje plaćenog umjetnika");
        log.info("11. Pronađi najmanje plaćenog radnika");
        log.info("12. Sortiraj pozornice po kapacitetu");
        log.info("13. Pretraži umjetnike po imenu");
        log.info("14. Pretraži članove po ulozi");
        log.info("15. Pretraži pozornice po nazivu");
        log.info("16. Pretraži izvedbe po umjetniku");
        log.info("17. Prikaži scenska imena umjetnika");
        log.info("0. Izlaz");
    }

    /**
     * Učitava ulogu člana od korisnika.
     * Ako korisnik unese vrijednost koja ne odgovara nijednoj vrijednosti
     * enumeracije {@link Member.Role}, prikazuje se upozorenje i unos se ponavlja.
     *
     * @return ispravna uloga člana unesena od strane korisnika
     */
    private static Member.Role readMemberRole() {
        while (true) {
            String roleInput = readString(
                    "Unesite ulogu člana " + Arrays.toString(Member.Role.values()) + ": "
            );

            try {
                return Member.Role.valueOf(roleInput.trim().toUpperCase());
            } catch (IllegalArgumentException _) {
                log.warn("Nepostojeća uloga člana: {}. Pokušajte ponovno.", roleInput);
            }
        }
    }

    private static void displayWorkersByVolunteerStatus(WorkerRepository workerRepository) {
        Map<Boolean, List<Worker>> workersByVolunteerStatus = workerRepository.getAllEntities()
                .stream()
                .collect(Collectors.partitioningBy(worker -> Worker.Role.VOLONTER.equals(worker.getRole())));

        log.info("Volonteri: {}", workersByVolunteerStatus.get(true));
        log.info("Plaćeni radnici: {}", workersByVolunteerStatus.get(false));
    }
}
