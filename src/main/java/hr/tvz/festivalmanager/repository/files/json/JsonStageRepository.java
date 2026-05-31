package hr.tvz.festivalmanager.repository.files.json;

import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.StageRepository;
import hr.tvz.festivalmanager.repository.files.json.dto.StageJson;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.json.bind.JsonbConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonStageRepository implements StageRepository {
    private static final Logger logger = LoggerFactory.getLogger(JsonStageRepository.class);
    private static final Path STAGES_FILE_PATH = Path.of("src/main/resources/data/stages.json");
    private final List<Stage> stages = new ArrayList<>();

    @Override
    public void inputAllEntities() {
        try {
            String json = Files.readString(STAGES_FILE_PATH);
            Jsonb jsonb = JsonbBuilder.create();

            Type stageListType = new ArrayList<StageJson>() {
            }.getClass().getGenericSuperclass();

            List<StageJson> stageJsonList = jsonb.fromJson(json, stageListType);

            stages.clear();

            for (StageJson stageJson : stageJsonList) {
                Stage stage = new Stage(stageJson.getName(), stageJson.getCapacity());
                stages.add(stage);
            }

            logger.info("Stage podaci uspješno su učitani iz JSON datoteke.");
        } catch (IOException exception){
            logger.error("Dogodila se pogreška kod čitanja stageova iz JSON datoteke.", exception);
        }
    }

    @Override
    public List<Stage> getAllEntities() {
        return stages;
    }

    private void saveAllEntities() {
        List<StageJson> stageJsonList = new ArrayList<>();

        for (Stage stage : stages) {
            StageJson stageJson = new StageJson(stage.name(), stage.capacity());
            stageJsonList.add(stageJson);
        }

        try {
            JsonbConfig config = new JsonbConfig()
                    .withFormatting(true);

            Jsonb jsonb = JsonbBuilder.create(config);
            String json = jsonb.toJson(stageJsonList);

            Files.writeString(STAGES_FILE_PATH, json);

            logger.info("Stage podaci uspješno su spremljeni u JSON datoteku.");
        } catch (IOException exception) {
            logger.error("Dogodila se pogreška kod spremanja stageova u JSON datoteku.", exception);
        }
    }

    public void saveEntity(Stage stage) {
        stages.add(stage);
        saveAllEntities();
    }
}
