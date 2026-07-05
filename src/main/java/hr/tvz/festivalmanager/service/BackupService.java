package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.repository.ArtistRepository;
import hr.tvz.festivalmanager.repository.MemberRepository;
import hr.tvz.festivalmanager.repository.PerformanceRepository;
import hr.tvz.festivalmanager.repository.StageRepository;
import hr.tvz.festivalmanager.repository.WorkerRepository;
import hr.tvz.festivalmanager.repository.files.backup.FestivalBackup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Servis za izradu i vraćanje pričuvne kopije svih podataka aplikacije.
 */
public class BackupService {
    private static final Logger log = LoggerFactory.getLogger(BackupService.class);
    private static final String BACKUP_FILE_NAME = "backup.bin";

    private final MemberRepository memberRepository;
    private final WorkerRepository workerRepository;
    private final ArtistRepository artistRepository;
    private final StageRepository stageRepository;
    private final PerformanceRepository performanceRepository;

    public BackupService(MemberRepository memberRepository,
                         WorkerRepository workerRepository,
                         ArtistRepository artistRepository,
                         StageRepository stageRepository,
                         PerformanceRepository performanceRepository) {
        this.memberRepository = memberRepository;
        this.workerRepository = workerRepository;
        this.artistRepository = artistRepository;
        this.stageRepository = stageRepository;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Serijalizira sve trenutne podatke u jednu binarnu datoteku "backup.bin".
     *
     * @throws IOException ako dođe do pogreške pri pisanju datoteke
     */
    public void createBackup() throws IOException {
        FestivalBackup backup = new FestivalBackup(
                memberRepository.getAllEntities(),
                workerRepository.getAllEntities(),
                artistRepository.getAllEntities(),
                stageRepository.getAllEntities(),
                performanceRepository.getAllEntities()
        );

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(BACKUP_FILE_NAME))) {
            out.writeObject(backup);
            log.info("Pričuvna kopija uspješno je spremljena u datoteku {}.", BACKUP_FILE_NAME);
        }
    }

    /**
     * Deserijalizira podatke iz pričuvne kopije i njima pregazi trenutne
     * podatke u svim repozitorijima.
     *
     * @throws IOException ako pričuvna kopija ne postoji ili je neispravna
     */
    public void restoreBackup() throws IOException {
        File backupFile = new File(BACKUP_FILE_NAME);
        if (!backupFile.exists()) {
            log.warn("Pričuvna kopija ne postoji. Prvo kreirajte kopiju.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(backupFile))) {
            FestivalBackup backup = (FestivalBackup) in.readObject();

            overwrite(memberRepository.getAllEntities(), backup.getMembers());
            overwrite(workerRepository.getAllEntities(), backup.getWorkers());
            overwrite(artistRepository.getAllEntities(), backup.getArtists());
            overwrite(stageRepository.getAllEntities(), backup.getStages());
            overwrite(performanceRepository.getAllEntities(), backup.getPerformances());

            log.info("Podaci su uspješno vraćeni iz pričuvne kopije.");
        } catch (ClassNotFoundException exception) {
            throw new IOException("Neispravan format pričuvne kopije.", exception);
        }
    }

    private static <T> void overwrite(List<T> target, List<T> source) {
        target.clear();
        target.addAll(source);
    }
}