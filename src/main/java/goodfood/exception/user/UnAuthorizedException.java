package goodfood.exception.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UnAuthorizedException extends RuntimeException{
    private String message;
}
