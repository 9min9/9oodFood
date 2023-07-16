package goodfood.controller.dto.user.passwordreset;

import goodfood.service.dto.user.PasswordResetServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@NoArgsConstructor(access = PUBLIC)
public class PasswordResetRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;

    @AssertTrue
    private Boolean emailAuth;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]*$")
    @Size(min = 8, max = 30)
    private String changePassword;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]*$")
    @Size(min = 8, max = 30)
    private String confirmPassword;

    public PasswordResetServiceDto toServiceDto() {
        return PasswordResetServiceDto.builder()
                .loginId(username).email(email).checkedEmailAuth(emailAuth)
                .changePassword(changePassword).confirmPassword(confirmPassword).build();
    }

}
