package hr.tvz.festivalmanager.repository.files.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Omotnica (wrapper) za listu {@link LogEntry} zapisa,
 * potrebna da JAXB može serijalizirati cijelu listu u jednu XML datoteku.
 */
@XmlRootElement(name = "log")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogEntries {

    @XmlElement(name = "logEntry")
    private List<LogEntry> entries = new ArrayList<>();

    public List<LogEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LogEntry> entries) {
        this.entries = entries;
    }
}