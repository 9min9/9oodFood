package goodfood.exception.mail;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MismatchEmailTokenException extends RuntimeException{
    private String message;

}
