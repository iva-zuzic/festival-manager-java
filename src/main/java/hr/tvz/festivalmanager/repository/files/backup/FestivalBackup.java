package hr.tvz.festivalmanager.repository.files.backup;

import hr.tvz.festivalmanager.entities.Artist;
import hr.tvz.festivalmanager.entities.Member;
import hr.tvz.festivalmanager.entities.Performance;
import hr.tvz.festivalmanager.entities.Stage;
import hr.tvz.festivalmanager.entities.Worker;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Serijalizabilna omotnica koja drži sve podatke aplikacije za potrebe
 * pričuvne kopije. Serijalizacijom jednog ovakvog objekta u "backup.bin"
 * čuvaju se sve liste odjednom, uz očuvane veze među entitetima.
 */
public class FestivalBackup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Member> members;
    private final List<Worker> workers;
    private final List<Artist> artists;
    private final List<Stage> stages;
    private final List<Performance> performances;

    public FestivalBackup(List<Member> members, List<Worker> workers,
                          List<Artist> artists, List<Stage> stages,
                          List<Performance> performances) {
        this.members = members;
        this.workers = workers;
        this.artists = artists;
        this.stages = stages;
        this.performances = performances;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public List<Performance> getPerformances() {
        return performances;
    }
}