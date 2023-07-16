package goodfood.service.dto.user;


import goodfood.entity.user.Gender;
import goodfood.entity.user.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static goodfood.entity.user.UserRole.ADMIN;
import static goodfood.entity.user.UserRole.USER;

@Getter
@Setter
public class UserServiceDto {
    private String loginId;
    private String password;

    private String confirmPassword;
    private String email;

    private boolean isEmailAuth;

    private String name;

    private String nickname;

    private String gender;

    private LocalDate birthDate;

    //TODO : Change UserRole
    public Member toEntity() {
        return Member.builder().loginId(loginId).password(password).email(email).emailAuth(isEmailAuth)
                .name(name).nickname(name).gender(Gender.valueOf(gender)).birthDate(birthDate).role(USER).build();
    }


}
