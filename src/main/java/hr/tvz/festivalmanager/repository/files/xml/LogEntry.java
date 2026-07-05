package hr.tvz.festivalmanager.repository.files.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Predstavlja jedan zapis u logu korisnikovih akcija.
 * Sadrži opis akcije i vrijeme kada je zabilježena.
 */
@XmlRootElement(name = "logEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogEntry {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

    @XmlElement
    private String action;

    @XmlElement
    private String timestamp;

    public LogEntry() {
    }

    public LogEntry(String action, LocalDateTime timestamp) {
        this.action = action;
        this.timestamp = timestamp.format(FORMATTER);
    }

    public String getAction() {
        return action;
    }

    public String getTimestamp() {
        return timestamp;
    }
}