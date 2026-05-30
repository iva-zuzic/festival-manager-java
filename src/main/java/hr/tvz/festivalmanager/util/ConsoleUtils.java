package hr.tvz.festivalmanager.util;

import hr.tvz.festivalmanager.exception.InvalidAmountException;
import hr.tvz.festivalmanager.exception.InvalidDataFormatException;
import hr.tvz.festivalmanager.exception.InvalidDateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * Pomoćna klasa za unos podataka putem konzole.
 * Sadrži metode za unos teksta, brojeva, datuma, vremena i email adresa.
 */
public class ConsoleUtils {
    private static final Logger log = LoggerFactory.getLogger(ConsoleUtils.class);
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    private ConsoleUtils() {
    }

    /**
     * Učitava tekstualni unos korisnika.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni tekst
     */
    public static String readString(String prompt) {
        log.info(prompt);
        return sc.nextLine();
    }

    /**
     * Učitava cijeli broj koji ne smije biti negativan.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni nenegativni cijeli broj
     */
    public static int readNonNegativeInt(String prompt) {
        return readIntWithMinimum(
                prompt,
                0,
                "Broj ne smije biti negativan!"
        );
    }

    /**
     * Učitava cijeli broj koji mora biti veći od nule.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni pozitivni cijeli broj
     */
    public static int readStrictlyPositiveInt(String prompt) {
        return readIntWithMinimum(
                prompt,
                1,
                "Broj mora biti veći od nule!"
        );
    }

    /**
     * Učitava cijeli broj koji mora biti veći ili jednak zadanoj minimalnoj vrijednosti.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @param minimumValue najmanja dopuštena vrijednost
     * @param errorMessage poruka koja se prikazuje kod neispravne vrijednosti
     * @return uneseni cijeli broj
     */
    private static int readIntWithMinimum(String prompt, int minimumValue, String errorMessage) {
        while (true) {
            log.info(prompt);

            try {
                int value = parseInteger(sc.nextLine());

                if (value < minimumValue) {
                    throw new InvalidAmountException(errorMessage);
                }

                return value;
            } catch (InvalidDataFormatException | InvalidAmountException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * Učitava cijeli broj unutar zadanog raspona.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @param min najmanja dopuštena vrijednost
     * @param max najveća dopuštena vrijednost
     * @return uneseni cijeli broj unutar raspona
     */
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            log.info(prompt);

            try {
                int value = parseInteger(sc.nextLine());

                if (value < min || value > max) {
                    log.warn("Broj mora biti između {} i {}!", min, max);
                    continue;
                }

                return value;
            } catch (InvalidDataFormatException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * Učitava decimalni broj koji ne smije biti negativan.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni nenegativni decimalni broj
     */
    public static BigDecimal readNonNegativeBigDecimal(String prompt) {
        while (true) {
            log.info(prompt);

            try {
                BigDecimal value = parseBigDecimal(sc.nextLine());

                if (value.compareTo(BigDecimal.ZERO) < 0) {
                    throw new InvalidAmountException("Iznos ne smije biti negativan!");
                }

                return value;
            } catch (InvalidDataFormatException | InvalidAmountException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * Učitava datum u formatu dd.MM.yyyy.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni datum
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            log.info(prompt);

            try {
                return parseDate(sc.nextLine());
            } catch (InvalidDateException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * Učitava datum i vrijeme u formatu dd.MM.yyyy. HH:mm.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @return uneseni datum i vrijeme
     */
    public static LocalDateTime readDateTime(String prompt) {
        while (true) {
            log.info(prompt);

            try {
                return LocalDateTime.parse(sc.nextLine(), DATETIME_FORMAT);
            } catch (DateTimeParseException _) {
                log.warn("Datum i vrijeme moraju biti u formatu dd.MM.yyyy. HH:mm");
            }
        }
    }

    /**
     * Pretvara tekstualni unos u cijeli broj.
     *
     * @param input tekstualni unos korisnika
     * @return cijeli broj
     * @throws InvalidDataFormatException ako unos nije cijeli broj
     */
    private static int parseInteger(String input) throws InvalidDataFormatException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            throw new InvalidDataFormatException("Morate unijeti cijeli broj!", exception);
        }
    }

    /**
     * Pretvara tekstualni unos u decimalni broj.
     *
     * @param input tekstualni unos korisnika
     * @return decimalni broj
     * @throws InvalidDataFormatException ako unos nije decimalni broj
     */
    private static BigDecimal parseBigDecimal(String input) throws InvalidDataFormatException {
        try {
            return new BigDecimal(input);
        } catch (NumberFormatException exception) {
            throw new InvalidDataFormatException("Morate unijeti decimalni broj!", exception);
        }
    }

    /**
     * Pretvara tekstualni unos u datum.
     *
     * @param input tekstualni unos korisnika
     * @return datum
     * @throws InvalidDateException ako unos nije u ispravnom formatu datuma
     */
    private static LocalDate parseDate(String input) throws InvalidDateException {
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException(
                    "Datum mora biti u formatu dd.MM.yyyy.",
                    exception
            );
        }
    }

    /**
     * Učitava email adresu i provjerava da je ispravnog formata i jedinstvena.
     * Email mora sadržavati znak @ i točku.
     * Ako email nije ispravan ili već postoji, unos se ponavlja.
     *
     * @param prompt poruka koja se prikazuje korisniku
     * @param existingEmails skup već unesenih email adresa
     * @return jedinstvena i ispravna email adresa
     */
    public static String readUniqueEmail(String prompt, Set<String> existingEmails) {
        while (true) {
            String email = readString(prompt).trim();

            if (!isValidEmail(email)) {
                log.warn("Email mora sadržavati znak @ i točku.");
                continue;
            }

            String normalizedEmail = email.toLowerCase(Locale.ROOT);

            if (existingEmails.add(normalizedEmail)) {
                return email;
            }

            log.warn("Email {} već postoji! Unesite drugi.", email);
        }
    }

    /**
     * Provjerava je li email adresa osnovno ispravnog formata.
     *
     * @param email email adresa koja se provjerava
     * @return {@code true} ako email sadrži znak @ i točku, inače {@code false}
     */
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}