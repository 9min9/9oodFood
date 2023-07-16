package goodfood.exception.forum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DuplicatedForumException extends RuntimeException{
    private String message;
}
