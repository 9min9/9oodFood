package goodfood.service.dto.mail;

import goodfood.entity.user.EmailAuth;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailServiceDto {
    private String email;
    private String authToken;

    public EmailAuth toDto() {
        return EmailAuth.builder().email(email).authToken(authToken).build();
    }

}
