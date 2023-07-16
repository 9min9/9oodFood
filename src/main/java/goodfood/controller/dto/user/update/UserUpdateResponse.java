package goodfood.controller.dto.user.update;

import goodfood.entity.user.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateResponse {
    private String username;
    private String name;
    private String email;
    private String nickname;

    private boolean emailAuth;
    private String gender;
    private int year;
    private int month;
    private int day;

    public UserUpdateResponse toDto(Member member) {
        this.username = member.getLoginId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.emailAuth = member.isEmailAuth();
        this.nickname = member.getNickname();
        this.gender = member.getGender().name();
        this.year = member.getBirthDate().getYear();
        this.month = member.getBirthDate().getMonthValue();
        this.day = member.getBirthDate().getDayOfMonth();

        return this;
    }

}
