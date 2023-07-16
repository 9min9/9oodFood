package goodfood.exception.user;

public class DuplicatedEmailException extends RuntimeException{
    private String message;

    public DuplicatedEmailException(String message) {
        this.message = message;
    }
}
