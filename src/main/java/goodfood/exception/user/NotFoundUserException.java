package goodfood.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundUserException extends RuntimeException{

    private String message;

    public NotFoundUserException(String message) {
        this.message = message;
    }
}
