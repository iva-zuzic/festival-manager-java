package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.entities.Worker;
import hr.tvz.festivalmanager.repository.WorkerRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servisna klasa za rad s radnicima.
 */
public class WorkerService {
    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    /**
     * Grupira radnike prema ulozi.
     *
     * @return mapa radnika po ulozi
     */
    public Map<Worker.Role, List<Worker>> groupByRole() {
        return workerRepository.getAllEntities().stream()
                .collect(Collectors.groupingBy(Worker::getRole));
    }

    /**
     * Dijeli radnike na volontere i radnike koji nisu volonteri.
     *
     * @return mapa radnika podijeljenih prema tome jesu li volonteri
     */
    public Map<Boolean, List<Worker>> partitionByVolunteer() {
        return workerRepository.getAllEntities().stream()
                .collect(Collectors.partitioningBy(w -> w.getRole() == Worker.Role.VOLONTER));
    }

    /**
     * Sortira radnike prema honoraru.
     *
     * @return lista radnika
     */
    public List<Worker> sortByFee() {
        return workerRepository.getAllEntities().stream()
                .sorted(Comparator.comparing(Worker::getFee))
                .toList();
    }

    /**
     * Računa ukupni iznos honorara svih radnika.
     *
     * @return ukupni honorar radnika
     */
    public BigDecimal calculateTotalWorkerFees() {
        return workerRepository.getAllEntities().stream()
                .map(Worker::getFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Pronalazi najmanje plaćenog radnika koji nije volonter.
     * Volonteri se izuzimaju jer su po definiciji neplaćeni, pa bi
     * inače uvijek bili prepoznati kao najmanje plaćeni.
     *
     * @return {@link Optional} s najmanje plaćenim neplaćenim radnikom,
     *         ili prazan {@link Optional} ako su svi radnici volonteri
     */
    public Optional<Worker> findLowestPaidNonVolunteer() {
        return workerRepository.getAllEntities().stream()
                .filter(worker -> worker.getRole() != Worker.Role.VOLONTER)
                .min(Comparator.comparing(Worker::getFee));
    }
}
