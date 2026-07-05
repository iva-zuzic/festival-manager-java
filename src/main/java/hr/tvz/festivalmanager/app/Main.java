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

        BackupService backupService = new BackupService(
                memberRepository, workerRepository, artistRepository,
                stageRepository, performanceRepository
        );

        XmlLogService xmlLogService = new XmlLogService();

        List<Person> allPersons = new ArrayList<>();
        PersonService.addPersonsToList(memberRepository.getAllEntities(), allPersons);
        PersonService.addPersonsToList(workerRepository.getAllEntities(), allPersons);

        ApplicationContext context = new ApplicationContext(
                memberService, workerService, artistService, stageService,
                performanceService, backupService, xmlLogService, workerRepository
        );

        processMenuChoice(allPersons, context);
    }

    public static void processMenuChoice(List<Person> allPersons, ApplicationContext context) {
        int choice;

        do {
            MenuPrinter.showMenu();
            choice = readNonNegativeInt("Odaberite opciju: ");
            context.xmlLogService().logAction("Odabrana opcija izbornika: " + choice);

            switch (choice) {
                case 1 -> MenuPrinter.printYoungestPerson(allPersons);
                case 2 -> MenuPrinter.printOldestPerson(allPersons);
                case 3 -> MenuPrinter.printHighestPaidArtist(context.artistService());
                case 4 -> MenuPrinter.printArtistsSortedByFee(context.artistService());
                case 5 -> MenuPrinter.printPerformancesByStage(context.performanceService());
                case 6 -> MenuPrinter.printMembersByRole(context.memberService(), context.artistService());
                case 7 -> MenuPrinter.printWorkerPartitioning(context.workerService());
                case 8 -> MenuPrinter.printArtistsByGenre(context.artistService());
                case 9 -> MenuPrinter.printHighestPaidWorker(context.workerRepository());
                case 10 -> MenuPrinter.printLowestPaidArtist(context.artistService());
                case 11 -> MenuPrinter.printLowestPaidWorker(context.workerService());
                case 12 -> MenuPrinter.printStagesSortedByCapacity(context.stageService());
                case 13 -> MenuPrinter.printTotalFees(context.artistService(), context.workerService());
                case 14 -> MenuPrinter.printFoundMembers(context.memberService());
                case 15 -> MenuPrinter.printAverageFeeByGenre(context.artistService());
                case 16 -> MenuPrinter.printFoundPerformances(context.performanceService());
                case 17 -> MenuPrinter.printArtistStageNames(context.artistService());
                case 18 -> MenuPrinter.createBackup(context.backupService());
                case 19 -> MenuPrinter.restoreBackup(context.backupService());
                case 20 -> context.xmlLogService().printLog();
                case 0 -> log.info("Doviđenja!");
                default -> log.warn("Nepoznata opcija: {}", choice);
            }

        } while (choice != 0);
    }
}