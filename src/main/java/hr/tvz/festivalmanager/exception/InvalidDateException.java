package hr.tvz.festivalmanager.exception;

/**
 * Predstavlja iznimku koja označava da uneseni datum nije ispravnog formata.
 * Koristi se kada korisnik unese datum u pogrešnom obliku ili u formatu
 * koji se ne može pretvoriti u LocalDate.
 */
public class InvalidDateException extends Exception {

    public InvalidDateException() {
        super("Uneseni datum nije ispravnog formata.");
    }

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(Throwable cause) {
        super(cause);
    }
}
