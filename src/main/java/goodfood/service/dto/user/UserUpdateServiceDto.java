package goodfood.service.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class UserUpdateServiceDto {
    private String loginId;
    private String nickname;
    private String name;

    private String originPassword;
    private String changedPassword;
    private String confirmPassword;

    private String email;
    private boolean checkedEmailAuth;
    private String gender;

    private String year;
    private String month;
    private String day;


}
