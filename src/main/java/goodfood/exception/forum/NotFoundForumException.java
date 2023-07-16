package goodfood.exception.forum;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundForumException extends RuntimeException {

    private String message;
    public NotFoundForumException(String message) {
        this.message = message;
    }



}
