package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Performance;
import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.ArtistRepository;
import hr.tvz.festivalmanager.repository.PerformanceRepository;
import hr.tvz.festivalmanager.repository.StageRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.PerformanceJson;
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
import java.util.List;
import java.util.Optional;

public class JsonPerformanceRepository implements PerformanceRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(JsonPerformanceRepository.class);

    private static final Path PERFORMANCES_FILE_PATH =
            Path.of("src/main/resources/data/performances.json");

    private final ArtistRepository artistRepository;
    private final StageRepository stageRepository;
    private final List<Performance> performances = new ArrayList<>();

    private static final class PerformanceJsonList extends ArrayList<PerformanceJson> {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    public JsonPerformanceRepository(ArtistRepository artistRepository,
                                     StageRepository stageRepository) {
        this.artistRepository = artistRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public void inputAllEntities() throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String json = Files.readString(PERFORMANCES_FILE_PATH);

            Type performanceListType =
                    PerformanceJsonList.class.getGenericSuperclass();

            List<PerformanceJson> performanceJsonList =
                    jsonb.fromJson(json, performanceListType);

            performances.clear();

            for (PerformanceJson performanceJson : performanceJsonList) {
                Optional<Artist> artist = artistRepository.getAllEntities()
                        .stream()
                        .filter(a -> a.getStageName().equals(performanceJson.getArtistStageName()))
                        .findFirst();

                Optional<Stage> stage = stageRepository.getAllEntities()
                        .stream()
                        .filter(s -> s.name().equals(performanceJson.getStageName()))
                        .findFirst();

                if (artist.isEmpty() || stage.isEmpty()) {
                    logger.warn(
                            "Preskačem izvedbu jer umjetnik ili pozornica nisu pronađeni: {} / {}",
                            performanceJson.getArtistStageName(),
                            performanceJson.getStageName()
                    );
                    continue;
                }

                Performance performance = new Performance(
                        artist.get(),
                        stage.get(),
                        performanceJson.getStart(),
                        performanceJson.getDuration()
                );

                performances.add(performance);
            }

            logger.info("Podaci o izvedbama uspješno su učitani iz JSON datoteke.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod parsiranja izvedbi iz JSON datoteke.",
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
    public List<Performance> getAllEntities() {
        return performances;
    }

    private void saveAllEntities() throws IOException {
        List<PerformanceJson> performanceJsonList = new ArrayList<>();

        for (Performance performance : performances) {
            PerformanceJson performanceJson = new PerformanceJson(
                    performance.getArtist().getStageName(),
                    performance.getStage().name(),
                    performance.getStart(),
                    performance.getDuration()
            );
            performanceJsonList.add(performanceJson);
        }

        JsonbConfig config = new JsonbConfig().withFormatting(true);

        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String json = jsonb.toJson(performanceJsonList);
            Files.writeString(PERFORMANCES_FILE_PATH, json);
            logger.info("Podaci o izvedbama uspješno su spremljeni u JSON datoteku.");
        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod pretvaranja izvedbi u JSON format.",
                    exception
            );
        } catch (Exception exception) {
            throw new IOException(
                    "Dogodila se pogreška kod zatvaranja JSON-B resursa.",
                    exception
            );
        }
    }

    public void saveEntity(Performance performance) throws IOException {
        performances.add(performance);
        saveAllEntities();
    }
}