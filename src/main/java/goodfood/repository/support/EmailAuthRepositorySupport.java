package goodfood.repository.support;

import goodfood.entity.user.EmailAuth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailAuthRepositorySupport {

    Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime);

    Optional<EmailAuth> findUsedToken(String email, boolean expired);

    List<EmailAuth> findUsedAllToken(boolean expired);



}
