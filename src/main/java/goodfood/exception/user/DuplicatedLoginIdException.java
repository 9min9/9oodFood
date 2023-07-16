package goodfood.exception.user;

import lombok.Getter;

public class DuplicatedLoginIdException extends RuntimeException{
    private String message;

    public DuplicatedLoginIdException(String message) {
        this.message = message;
    }

}
