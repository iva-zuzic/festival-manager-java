package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Worker;
import hr.tvz.festivalmanager.repository.WorkerRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.WorkerJson;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonWorkerRepository implements WorkerRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(JsonWorkerRepository.class);

    private static final Path WORKERS_FILE_PATH =
            Path.of("src/main/resources/data/workers.json");

    private final List<Worker> workers = new ArrayList<>();
    private final Set<String> existingEmails = new HashSet<>();

    private static final class WorkerJsonList extends ArrayList<WorkerJson> {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Override
    public void inputAllEntities() throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String json = Files.readString(WORKERS_FILE_PATH);

            Type workerListType =
                    WorkerJsonList.class.getGenericSuperclass();

            List<WorkerJson> workerJsonList =
                    jsonb.fromJson(json, workerListType);

            workers.clear();
            existingEmails.clear();

            for (WorkerJson workerJson : workerJsonList) {
                Worker worker = new Worker(
                        workerJson.getFirstName(),
                        workerJson.getLastName(),
                        workerJson.getEmail(),
                        workerJson.getDateOfBirth(),
                        workerJson.getRole(),
                        workerJson.getFee()
                );

                workers.add(worker);
                existingEmails.add(worker.getEmail());
            }

            logger.info("Worker(radnik) podaci uspješno su učitani iz JSON datoteke.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod parsiranja radnika iz JSON datoteke.",
                    exception
            );
        } catch (Exception exception) {
            throw new IOException(
                    "Dogodila se pogreška kod zatvaranja JSON-B resursa.",
                    exception
            );
        }
    }

    @Override
    public List<Worker> getAllEntities() {
        return workers;
    }

    private void saveAllEntities() throws IOException {
        List<WorkerJson> workerJsonList = new ArrayList<>();

        for (Worker worker : workers) {
            WorkerJson workerJson = new WorkerJson(
                    worker.getFirstName(),
                    worker.getLastName(),
                    worker.getEmail(),
                    worker.getDateOfBirth(),
                    worker.getRole(),
                    worker.getFee()
            );

            workerJsonList.add(workerJson);
        }

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);

        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String json = jsonb.toJson(workerJsonList);

            Files.writeString(WORKERS_FILE_PATH, json);

            logger.info("Worker(radnik) podaci uspješno su spremljeni u JSON datoteku.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod pretvaranja radnika u JSON format.",
                    exception
            );
        } catch (Exception exception) {
            throw new IOException(
                    "Dogodila se pogreška kod zatvaranja JSON-B resursa.",
                    exception
            );
        }
    }

    public void saveEntity(Worker worker) throws IOException {
        if (existingEmails.contains(worker.getEmail())) {
            logger.warn(
                    "Radnik s email adresom {} već postoji i neće biti ponovno spremljen.",
                    worker.getEmail()
            );
            return;
        }

        workers.add(worker);
        existingEmails.add(worker.getEmail());
        saveAllEntities();
    }
}