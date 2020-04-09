package exception.validator;

public class EmptyFieldException extends ValidationException {
    public EmptyFieldException(String message) {
        super(message);
    }
}
