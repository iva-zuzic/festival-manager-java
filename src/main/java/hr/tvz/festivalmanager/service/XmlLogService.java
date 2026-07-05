package hr.tvz.festivalmanager.service;

import hr.tvz.festivalmanager.repository.files.xml.LogEntries;
import hr.tvz.festivalmanager.repository.files.xml.LogEntry;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Servis za bilježenje korisnikovih akcija u XML datoteku i njihov ispis.
 */
public class XmlLogService {
    private static final Logger log = LoggerFactory.getLogger(XmlLogService.class);
    private static final String LOG_FILE_NAME = "action-log.xml";

    /**
     * Bilježi jednu akciju dodavanjem novog zapisa u XML datoteku.
     * Postojeći zapisi se prvo učitaju kako se ne bi izgubili.
     *
     * @param action opis akcije koju je korisnik napravio
     */
    public void logAction(String action) {
        try {
            JAXBContext context = JAXBContext.newInstance(LogEntries.class);

            LogEntries logEntries = readExisting(context);
            logEntries.getEntries().add(new LogEntry(action, LocalDateTime.now()));

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(logEntries, new File(LOG_FILE_NAME));
        } catch (JAXBException exception) {
            log.error("Greška pri zapisivanju akcije u XML log.", exception);
        }
    }

    /**
     * Ispisuje sve zabilježene akcije bez XML tagova, samo vrijednosti.
     */
    public void printLog() {
        File logFile = new File(LOG_FILE_NAME);
        if (!logFile.exists()) {
            log.info("Log je prazan.");
            return;
        }

        try {
            JAXBContext context = JAXBContext.newInstance(LogEntries.class);
            LogEntries logEntries = readExisting(context);

            log.info("Zabilježene akcije:");
            for (LogEntry entry : logEntries.getEntries()) {
                log.info("- [{}] {}", entry.getTimestamp(), entry.getAction());
            }
        } catch (JAXBException exception) {
            log.error("Greška pri čitanju XML loga.", exception);
        }
    }

    private LogEntries readExisting(JAXBContext context) throws JAXBException {
        File logFile = new File(LOG_FILE_NAME);
        if (!logFile.exists()) {
            return new LogEntries();
        }
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (LogEntries) unmarshaller.unmarshal(logFile);
    }
}