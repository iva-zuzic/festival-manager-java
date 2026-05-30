package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Performance;
import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.repository.PerformanceRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

/**
 * Servisna klasa za rad s izvedbama.
 */
public class PerformanceService {
    private final PerformanceRepository performanceRepository;

    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    /**
     * Pretražuje izvedbe prema scenskom imenu umjetnika.
     *
     * @param artistName scensko ime umjetnika
     * @return lista izvedbi
     */
    public List<Performance> findByArtistName(String artistName) {
        List<Performance> allPerformances = performanceRepository.getAllEntities();
        List<Performance> resultList = new ArrayList<>();

        for (Performance performance : allPerformances) {
            if (performance.getArtist().getStageName().equalsIgnoreCase(artistName)) {
                resultList.add(performance);
            }
        }
        return resultList;
    }

    /**
     * Grupira izvedbe prema pozornici.
     *
     * @return mapa izvedbi po pozornici
     */
    public Map<Stage, List<Performance>> groupByStage() {
        return performanceRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(Performance::getStage));
    }

    /**
     * Pronalazi izvedbu s najkraćim trajanjem.
     *
     * @return najkraća izvedba
     */
    public Optional<Performance> getShortestPerformance() {
        return performanceRepository.getAllEntities().stream()
                .min(Comparator.comparingInt(Performance::getDuration));
    }

    /**
     * Pronalazi izvedbu s najdužim trajanjem.
     *
     * @return najduža izvedba
     */
    public Optional<Performance> getLongestPerformance() {
        return performanceRepository.getAllEntities().stream()
                .max(Comparator.comparingInt(Performance::getDuration));
    }

    /**
     * Dijeli izvedbe u blokove po dvije izvedbe.
     *
     * @return lista blokova izvedbi
     */
    public List<List<Performance>> splitPerformancesIntoPairs() {
        return performanceRepository.getAllEntities().stream()
                .gather(Gatherers.windowFixed(2))
                .toList();
    }
}
