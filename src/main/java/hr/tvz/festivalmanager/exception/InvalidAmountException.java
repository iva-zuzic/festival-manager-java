package hr.tvz.festivalmanager.exception;

/**
 * Iznimka koja se baca kada je unesena nedopuštena brojčana vrijednost.
 * Koristi se kod unosa iznosa, cijena, kapaciteta ili drugih vrijednosti
 * koje ne smiju biti negativne ili moraju biti veće od nule.
 */
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Unesena brojčana vrijednost nije ispravna.");
    }

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAmountException(Throwable cause) {
        super(cause);
    }
}
