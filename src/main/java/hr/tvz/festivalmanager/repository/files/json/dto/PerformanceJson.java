package hr.tvz.festivalmanager.repository.files.json.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;

import java.time.LocalDateTime;

public class PerformanceJson {

    private String artistStageName;
    private String stageName;

    @JsonbDateFormat("dd.MM.yyyy. HH:mm")
    private LocalDateTime start;

    private int duration;

    public PerformanceJson() {
    }

    public PerformanceJson(String artistStageName, String stageName,
                           LocalDateTime start, int duration) {
        this.artistStageName = artistStageName;
        this.stageName = stageName;
        this.start = start;
        this.duration = duration;
    }

    public String getArtistStageName() {
        return artistStageName;
    }

    public void setArtistStageName(String artistStageName) {
        this.artistStageName = artistStageName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}