package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.StageRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Servisna klasa za rad s pozornicama.
 */
public class StageService {
    private final StageRepository stageRepository;

    public StageService(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    /**
     * Pretražuje pozornice prema nazivu.
     *
     * @param name naziv pozornice
     * @return lista pozornica
     */
    public List<Stage> findByName(String name) {
        List<Stage> allStages = stageRepository.getAllEntities();
        List<Stage> resultList = new ArrayList<>();

        for (Stage stage : allStages) {
            if (stage.name().equalsIgnoreCase(name)) {
                resultList.add(stage);
            }
        }
        return resultList;
    }

    /**
     * Sortira pozornice prema kapacitetu.
     *
     * @return lista pozornica
     */
    public List<Stage> sortByCapacity() {
        return stageRepository.getAllEntities().stream()
                .sorted(Comparator.comparingInt(Stage::capacity))
                .toList();
    }

    /**
     * Pronalazi pozornicu s najmanjim kapacitetom.
     *
     * @return pozornica s najmanjim kapacitetom
     */
    public Optional<Stage> stageWithLowestCapacity() {
        return stageRepository.getAllEntities().stream()
                .min(Comparator.comparingInt(Stage::capacity));
    }

    /**
     * Pronalazi pozornicu s najvećim kapacitetom.
     *
     * @return pozornica s najvećim kapacitetom
     */
    public Optional<Stage> stageWithHighestCapacity() {
        return stageRepository.getAllEntities().stream()
                .max(Comparator.comparingInt(Stage::capacity));
    }
}
