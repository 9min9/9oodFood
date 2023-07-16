package goodfood.exception.store;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NotFoundStoreException extends RuntimeException{
    private String message;
}
