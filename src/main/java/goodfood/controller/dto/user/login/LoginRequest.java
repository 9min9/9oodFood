package goodfood.controller.dto.user.login;

import goodfood.service.dto.user.LoginServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@NoArgsConstructor(access = PUBLIC)
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginServiceDto toServiceDto() {
        LoginServiceDto serviceDto = new LoginServiceDto();
        serviceDto.setLoginId(username);
        serviceDto.setPassword(password);
        return serviceDto;
    }
}
