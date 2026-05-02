/**
 * ATMException - Custom exception for ATM business rule violations.
 * Demonstrates: Custom Exception Handling (extends Exception).
 * Team: Vision Coders | ATM Simulation System
 */
public class ATMException extends Exception {

    public ATMException(String message) {
        super(message);
    }

    public ATMException(String message, Throwable cause) {
        super(message, cause);
    }
}
