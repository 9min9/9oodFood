package goodfood.controller.dto.user.signup;

import goodfood.service.dto.user.UserServiceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static lombok.AccessLevel.PUBLIC;

@Getter
@Setter
@NoArgsConstructor(access = PUBLIC)
public class SignUpRequest {
    @NotBlank
    @Pattern(regexp = "^[a-z][a-z0-9]*$")
    @Size(min =5, max = 20)
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]*$")
    @Size(min = 8, max = 30)
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]*$")
    @Size(min = 8, max = 30)
    private String confirmPassword;
    @NotBlank
    @Email
    private String email;

    @AssertTrue
    private Boolean emailAuth;

    @NotBlank
    @Pattern(regexp = "^[가-힣]+$")

    @Length(min = 2, max = 5)
    private String name;
    @NotBlank
    private String gender;

    @NotNull
    @Range(min = 1900, max = 2023)
    private Integer year;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;

    @NotNull
    @Range(min = 1, max = 31)
    private Integer day;

    public UserServiceDto toServiceDto() {
        UserServiceDto serviceDto = new UserServiceDto();
        serviceDto.setLoginId(username);
        serviceDto.setEmail(email);
        serviceDto.setEmailAuth(emailAuth);
        serviceDto.setPassword(password);
        serviceDto.setConfirmPassword(confirmPassword);
        serviceDto.setName(name);
        serviceDto.setGender(gender);
        serviceDto.setBirthDate(LocalDate.of(year, month, day));
        serviceDto.setNickname(name);

        return serviceDto;
    }

}
