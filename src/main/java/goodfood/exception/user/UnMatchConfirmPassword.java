package goodfood.exception.user;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PUBLIC;

@NoArgsConstructor(access = PUBLIC)
public class UnMatchConfirmPassword extends RuntimeException{

    private String message;

    public UnMatchConfirmPassword(String message) {
        this.message = message;
    }
}
