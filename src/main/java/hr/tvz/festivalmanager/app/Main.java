package hr.tvz.festivalmanager.app;

import hr.tvz.festivalmanager.entities.*;
import hr.tvz.festivalmanager.presentation.MenuPrinter;
import hr.tvz.festivalmanager.repository.*;
import hr.tvz.festivalmanager.repository.files.json.JsonMemberRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonStageRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonWorkerRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonArtistRepository;
import hr.tvz.festivalmanager.repository.files.json.JsonPerformanceRepository;
import hr.tvz.festivalmanager.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static hr.tvz.festivalmanager.util.ConsoleUtils.readNonNegativeInt;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private Main() {}

    static void main() {
        try {
            startApplication();
        } catch (IOException exception) {
            log.error("Dogodila se pogreška pri učitavanju ili spremanju podataka.", exception);
        } catch (RuntimeException exception) {
            log.error("Aplikacija je prekinuta zbog neočekivane pogreške.", exception);
        }
    }

    private static void startApplication() throws IOException {
        MemberRepository memberRepository = new JsonMemberRepository();
        WorkerRepository workerRepository = new JsonWorkerRepository();
        ArtistRepository artistRepository = new JsonArtistRepository(memberRepository);
        StageRepository stageRepository = new JsonStageRepository();
        PerformanceRepository performanceRepository = new JsonPerformanceRepository(artistRepository, stageRepository);

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
        PersonService.addPersonsToList(memberRepository.getAllEntities(), allPersons);
        PersonService.addPersonsToList(workerRepository.getAllEntities(), allPersons);

        processMenuChoice(
                allPersons,
                memberService,
                workerService,
                artistService,
                stageService,
                performanceService,
                workerRepository
        );
    }

    public static void processMenuChoice(List<Person> allPersons,
                                         MemberService memberService,
                                         WorkerService workerService,
                                         ArtistService artistService,
                                         StageService stageService,
                                         PerformanceService performanceService,
                                         WorkerRepository workerRepository) {
        int choice;

        do {
            MenuPrinter.showMenu();
            choice = readNonNegativeInt("Odaberite opciju: ");

            switch (choice) {
                case 1 -> MenuPrinter.printYoungestPerson(allPersons);
                case 2 -> MenuPrinter.printOldestPerson(allPersons);
                case 3 -> MenuPrinter.printHighestPaidArtist(artistService);
                case 4 -> MenuPrinter.printArtistsSortedByFee(artistService);
                case 5 -> MenuPrinter.printPerformancesByStage(performanceService);
                case 6 -> MenuPrinter.printMembersByRole(memberService, artistService);
                case 7 -> MenuPrinter.printWorkerPartitioning(workerService);
                case 8 -> MenuPrinter.printArtistsByGenre(artistService);
                case 9 -> MenuPrinter.printHighestPaidWorker(workerRepository);
                case 10 -> MenuPrinter.printLowestPaidArtist(artistService);
                case 11 -> MenuPrinter.printLowestPaidWorker(workerService);
                case 12 -> MenuPrinter.printStagesSortedByCapacity(stageService);
                case 13 -> MenuPrinter.printTotalFees(artistService, workerService);
                case 14 -> MenuPrinter.printFoundMembers(memberService);
                case 15 -> MenuPrinter.printAverageFeeByGenre(artistService);
                case 16 -> MenuPrinter.printFoundPerformances(performanceService);
                case 17 -> MenuPrinter.printArtistStageNames(artistService);
                case 0 -> log.info("Doviđenja!");
                default -> log.warn("Nepoznata opcija: {}", choice);
            }

        } while (choice != 0);
    }
}