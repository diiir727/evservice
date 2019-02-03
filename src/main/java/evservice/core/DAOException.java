package evservice.core;

/**
 * Сигнализирует о проблемах в работе DAO
 */
public class DAOException extends Exception {

    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
