package goodfood.controller.dto.admin.user;

import goodfood.entity.user.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserConfigResponse {

    private Long id;
    private String loginId;
    private String nickName;
    private String name;
    private String gender;
    private LocalDate createDate;
    private LocalDate birthDate;
    private int age;
    private String role;

    public UserConfigResponse toDto(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.nickName = member.getNickname();
        this.name = member.getName();
        this.gender = member.getGender().getKorName();
        this.createDate = member.getCreateDate().toLocalDate();
        this.birthDate = member.getBirthDate();
        this.role = member.getRole().name();
        this.age = member.calcAge();

        return this;
    }



}
