package goodfood.controller.dto.mail;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PUBLIC;

@Getter
@Setter
@NoArgsConstructor(access = PUBLIC)
public class ValidMailAuthTokenRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String token;
}
