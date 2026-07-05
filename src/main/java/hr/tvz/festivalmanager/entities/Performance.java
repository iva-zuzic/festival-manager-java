package hr.tvz.festivalmanager.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Predstavlja izvedbu umjetnika na određenoj pozornici festivala.
 * Izvedba povezuje umjetnika, pozornicu, vrijeme početka i trajanje nastupa.
 * Klasa implementira sučelje {@link Schedulable}, što znači da ima definirano
 * vrijeme početka i vrijeme završetka.
 */
public class Performance implements Schedulable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
    private Artist artist;
    private Stage stage;
    private LocalDateTime start;
    private int duration;

    public Performance(Artist artist, Stage stage, LocalDateTime start, int duration) {
        this.artist = artist;
        this.stage = stage;
        this.start = start;
        this.duration = duration;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Performance that = (Performance) o;
        return duration == that.duration && Objects.equals(artist, that.artist) && Objects.equals(stage, that.stage) && Objects.equals(start, that.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artist, stage, start, duration);
    }

    @Override
    public String toString() {
        return "Izvedba{" +
                "umjetnik: " + artist +
                ", pozornica: " + stage +
                ", početak: " + start +
                ", trajanje: " + duration +
                '}';
    }

    @Override
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Vraća vrijeme završetka izvedbe.
     * Vrijeme završetka računa se dodavanjem trajanja izvedbe u minutama
     * na vrijeme početka izvedbe.
     *
     * @return vrijeme završetka izvedbe
     */
    @Override
    public LocalDateTime getEnd() {
        return start.plusMinutes(duration);
    }

    public String getFormattedStart() {
        return start.format(DATE_TIME_FORMATTER);
    }
}
