package hr.tvz.festivalmanager.exception;

/**
 * Iznimka koja se baca kada traženi entitet nije pronađen u aplikaciji.
 * Koristi se u situacijama kada se očekuje postojanje barem jednog objekta,
 * primjerice kod pronalaženja najmlađe, najstarije ili najplaćenije osobe.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
}
