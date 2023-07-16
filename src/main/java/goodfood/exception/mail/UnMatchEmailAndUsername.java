package goodfood.exception.mail;

public class UnMatchEmailAndUsername extends RuntimeException{
    private String message;

    public UnMatchEmailAndUsername(String message) {
        this.message = message;
    }
}
