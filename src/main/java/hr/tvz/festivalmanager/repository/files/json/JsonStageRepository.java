package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.StageRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.StageJson;
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

public class JsonStageRepository implements StageRepository {

    private static final Logger logger =
            LoggerFactory.getLogger(JsonStageRepository.class);

    private static final Path STAGES_FILE_PATH =
            Path.of("src/main/resources/data/stages.json");

    private final List<Stage> stages = new ArrayList<>();

    private static final class StageJsonList extends ArrayList<StageJson> {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Override
    public void inputAllEntities() throws IOException {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String json = Files.readString(STAGES_FILE_PATH);

            Type stageListType =
                    StageJsonList.class.getGenericSuperclass();

            List<StageJson> stageJsonList =
                    jsonb.fromJson(json, stageListType);

            stages.clear();

            for (StageJson stageJson : stageJsonList) {
                Stage stage = new Stage(
                        stageJson.getName(),
                        stageJson.getCapacity()
                );
                stages.add(stage);
            }

            logger.info("Stage(pozornica) podaci uspješno su učitani iz JSON datoteke.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod parsiranja pozornica iz JSON datoteke.",
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
    public List<Stage> getAllEntities() {
        return stages;
    }

    private void saveAllEntities() throws IOException {
        List<StageJson> stageJsonList = new ArrayList<>();

        for (Stage stage : stages) {
            StageJson stageJson = new StageJson(
                    stage.name(),
                    stage.capacity()
            );
            stageJsonList.add(stageJson);
        }

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);

        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String json = jsonb.toJson(stageJsonList);

            Files.writeString(STAGES_FILE_PATH, json);

            logger.info("Stage podaci uspješno su spremljeni u JSON datoteku.");

        } catch (JsonbException exception) {
            throw new IOException(
                    "Dogodila se pogreška kod pretvaranja pozornica u JSON format.",
                    exception
            );
        } catch (Exception exception) {
            throw new IOException(
                    "Dogodila se pogreška kod zatvaranja JSON-B resursa.",
                    exception
            );
        }
    }

    public void saveEntity(Stage stage) throws IOException {
        stages.add(stage);
        saveAllEntities();
    }
}