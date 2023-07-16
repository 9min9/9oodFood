package goodfood.controller.dto.user.update;

import goodfood.service.dto.user.UserUpdateServiceDto;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

import static lombok.AccessLevel.PUBLIC;

@Builder
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PUBLIC)
@Getter @Setter
public class UserUpdateRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(max = 10)
    private String nickname;
    @NotBlank(message = "NotBlank")
    @Pattern(regexp = "^[가-힣]{2,5}$")
    @Size(min = 2, max = 5)
    private String name;

    private String password;

    private String changePassword;
    private String confirmPassword;
    @Email
    @NotBlank
    private String email;
    private Boolean emailAuth;
    @NotBlank
    private String gender;
    @NotNull
    @Range(min = 1900, max = 2023)
    private String year;

    @NotNull
    @Range(min = 1, max = 12)
    private String month;
    @NotNull
    @Range(min = 1, max = 31)
    private String day;

    public UserUpdateServiceDto toUserUpdateServiceDto() {
        return UserUpdateServiceDto.builder().loginId(username).nickname(nickname).name(name)
                .originPassword(password).changedPassword(changePassword).confirmPassword(confirmPassword)
                .email(email).checkedEmailAuth(emailAuth).gender(gender).year(year).month(month).day(day).build();
    }



}
