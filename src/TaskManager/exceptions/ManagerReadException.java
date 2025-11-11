package TaskManager.exceptions;

public class ManagerReadException extends RuntimeException {
    public ManagerReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerReadException(String message) {
        super(message);
    }
}
