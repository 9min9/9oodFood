package goodfood.exception.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DuplicatedNicknameException extends RuntimeException{
    private String message;
}
