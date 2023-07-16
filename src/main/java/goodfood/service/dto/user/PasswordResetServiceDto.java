package goodfood.service.dto.user;


import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetServiceDto {
    
    private String loginId;
    private String email;
    private boolean checkedEmailAuth;
    private String changePassword;
    private String confirmPassword;


}
