package hr.tvz.festivalmanager.presentation;

import hr.tvz.festivalmanager.entities.*;
import hr.tvz.festivalmanager.repository.WorkerRepository;
import hr.tvz.festivalmanager.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static hr.tvz.festivalmanager.util.ConsoleUtils.readString;

public class MenuPrinter {

    private static final Logger log = LoggerFactory.getLogger(MenuPrinter.class);
    private static final String FIRSTNAME_LASTNAME_FORMAT = "- {} {}";
    private static final String ITEM_FORMAT = "- {}";

    private MenuPrinter(){}

    public static void showMenu() {
        log.info("=== IZBORNIK ===");
        log.info("1. Pronađi najmlađu osobu");
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
        log.info("13. Prikaži ukupni trošak honorara");
        log.info("14. Pretraži članove po ulozi");
        log.info("15. Prikaži prosječni honorar po žanru");
        log.info("16. Pretraži izvedbe po umjetniku");
        log.info("17. Prikaži listu svih umjetnika");
        log.info("0. Izlaz");
    }

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

    public static void printYoungestPerson(List<Person> persons) {
        log.info("Najmlađa osoba: {}", PersonService.findYoungest(persons));
    }

    public static void printOldestPerson(List<Person> persons) {
        log.info("Najstarija osoba: {}", PersonService.findOldest(persons));
    }

    public static void printPerformancesByStage(PerformanceService performanceService) {
        log.info("Izvedbe grupirane po pozornici:");
        performanceService.groupByStage().forEach((stage, performances) -> {
            log.info("{}:", stage.name());
            performances.forEach(performance ->
                    log.info("- {}", performance.getArtist().getStageName()));
        });
    }

    public static void printMembersByRole(MemberService memberService, ArtistService artistService) {
        log.info("Članovi grupirani po ulozi:");
        memberService.groupByRole().forEach((role, members) -> {
            log.info("{}:", role);
            members.forEach(member -> {
                List<String> bands = artistService.findBandsForMember(member);
                log.info("- {} {} (bendovi: {})",
                        member.getFirstName(), member.getLastName(),
                        bands.isEmpty() ? "nema" : String.join(", ", bands));
            });
        });
    }

    public static void printWorkerPartitioning(WorkerService workerService) {
        Map<Boolean, List<Worker>> workersByVolunteerStatus = workerService.partitionByVolunteer();
        log.info("Volonteri:");
        workersByVolunteerStatus.get(true).forEach(volunteer -> log.info(FIRSTNAME_LASTNAME_FORMAT, volunteer.getFirstName(), volunteer.getLastName()));
        log.info("Plaćeni radnici:");
        workersByVolunteerStatus.get(false).forEach(paidWorker -> log.info(FIRSTNAME_LASTNAME_FORMAT, paidWorker.getFirstName(), paidWorker.getLastName()));
    }

    public static void printHighestPaidArtist(ArtistService artistService) {
        Artist artist = artistService.getHighestPaidArtist();
        log.info("Najplaćeniji umjetnik: {} ({} EUR)", artist.getStageName(), artist.getFee());
    }

    public static void printArtistsSortedByFee(ArtistService artistService) {
        log.info("Umjetnici sortirani po honoraru:");
        artistService.sortByFee().forEach(artist -> log.info("- {} | Honorar: {} EUR", artist.getStageName(), artist.getFee()));
    }

    public static void printArtistsByGenre(ArtistService artistService) {
        log.info("Umjetnici grupirani po žanru:");
        artistService.groupByGenre().forEach((genre, artists) -> {
            log.info("{}:", genre);
            artists.forEach(artist -> log.info(ITEM_FORMAT, artist.getStageName()));
        });
    }

    public static void printHighestPaidWorker(WorkerRepository workerRepository) {
        Worker worker = PersonService.findHighestPaid(workerRepository.getAllEntities());
        log.info("Najplaćeniji radnik: {} {} ({} EUR)",
                worker.getFirstName(), worker.getLastName(), worker.getFee());
    }

    public static void printLowestPaidArtist(ArtistService artistService) {
        Artist artist = artistService.getLowestPaidArtist();
        log.info("Najmanje plaćeni umjetnik: {} ({} EUR)", artist.getStageName(), artist.getFee());
    }

    public static void printLowestPaidWorker(WorkerService workerService) {
        workerService.findLowestPaidNonVolunteer().ifPresentOrElse(
                worker -> log.info("Najmanje plaćeni radnik: {} {} ({} EUR)",
                        worker.getFirstName(), worker.getLastName(), worker.getFee()),
                () -> log.info("Nema plaćenih radnika.")
        );
    }

    public static void printStagesSortedByCapacity(StageService stageService) {
        log.info("Pozornice sortirane po kapacitetu:");
        stageService.sortByCapacity().forEach(stage ->
                log.info("- {} ({} mjesta)", stage.name(), stage.capacity()));
    }

    public static void printTotalFees(ArtistService artistService, WorkerService workerService) {
        BigDecimal artistFees = artistService.calculateTotalArtistFees();
        BigDecimal workerFees = workerService.calculateTotalWorkerFees();
        BigDecimal total = artistFees.add(workerFees);

        log.info("Ukupni trošak honorara:");
        log.info("- Umjetnici: {} EUR", artistFees);
        log.info("- Radnici: {} EUR", workerFees);
        log.info("- UKUPNO: {} EUR", total);
    }

    public static void printFoundMembers(MemberService memberService) {
        List<Member> members = memberService.findByRole(readMemberRole());
        log.info("Pronađeni članovi:");
        if (members.isEmpty()) {
            log.info("- nema pronađenih članova");
            return;
        }
        members.forEach(member ->
                log.info(FIRSTNAME_LASTNAME_FORMAT, member.getFirstName(), member.getLastName()));
    }

    public static void printAverageFeeByGenre(ArtistService artistService) {
        log.info("Prosječni honorar po žanru:");
        artistService.averageFeeByGenre().forEach((genre, averageFee) ->
                log.info("- {}: {} EUR", genre, String.format("%.2f", averageFee)));
    }

    public static void printFoundPerformances(PerformanceService performanceService) {
        List<Performance> performances =
                performanceService.findByArtistName(readString("Unesite ime umjetnika: "));
        log.info("Pronađene izvedbe:");
        if (performances.isEmpty()) {
            log.info("- nema pronađenih izvedbi");
            return;
        }
        performances.forEach(performance ->
                log.info("- {} na pozornici {} ({})",
                        performance.getArtist().getStageName(),
                        performance.getStage().name(),
                        performance.getFormattedStart()));
    }

    public static void printArtistStageNames(ArtistService artistService) {
        artistService.getArtistStageNames().forEach(name -> log.info(ITEM_FORMAT, name));
    }
}
