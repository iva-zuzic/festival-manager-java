package hr.tvz.festivalmanager.exception;

/**
 * Predstavlja iznimku koja označava da unos nije ispravnog formata.
 * Koristi se kada korisnik unese podatak koji nije u očekivanom obliku,
 * primjerice tekst umjesto cijelog ili decimalnog broja.
 */
public class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException() {

        super("Krivi format.");
    }

    public InvalidDataFormatException(String message) {
        super(message);
    }

    public InvalidDataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataFormatException(Throwable cause) {
        super(cause);
    }
}
