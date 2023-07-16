package goodfood.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnMatchedPasswordException extends RuntimeException{

    private String message;

    public UnMatchedPasswordException(String message) {
        this.message = message;
    }
}
