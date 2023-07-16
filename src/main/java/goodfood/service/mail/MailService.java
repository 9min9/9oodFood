package goodfood.service.mail;

import goodfood.entity.user.EmailAuth;
import goodfood.entity.user.Member;
import goodfood.exception.mail.ExpiredEmailTokenException;
import goodfood.exception.mail.MismatchEmailTokenException;
import goodfood.exception.mail.UnMatchEmailAndUsername;
import goodfood.exception.user.NotFoundUserException;
import goodfood.repository.infra.EmailAuthRepository;
import goodfood.repository.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final JavaMailSender javaMailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final MemberRepository memberRepository;


    public void saveEmailToken(String email, String authToken) {
        EmailAuth emailAuth = EmailAuth.builder().email(email).authToken(authToken).expired(false).build();
        emailAuthRepository.save(emailAuth);
    }

    public String createAuthToken(int size) {
        String randomStr = UUID.randomUUID().toString().replaceAll("[^a-zA-Z^0-9]", "").substring(0, size);
        return randomStr;
    }


    public boolean checkLoginIdAndEmail(String loginId, String email) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(NotFoundUserException::new);
        if(member.getEmail().equals(email)) {
            return true;
        } else {
            throw new UnMatchEmailAndUsername("아이디와 메일이 일치하지 않습니다.");
        }
    }

    @Async
    public void sendEmail(String email, String authToken, String message) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setFrom("9oodFood.com");
        smm.setSubject(message);
        smm.setText(authToken);

        javaMailSender.send(smm);
    }

    public boolean isValidEmailToken(String email, String authToken) {
        Optional<EmailAuth> findEmail = emailAuthRepository.findValidAuthByEmail(email, authToken, LocalDateTime.now());
        if(findEmail.isEmpty()) {
            throw new MismatchEmailTokenException();
        } else {
            return true;
        }
    }

    public boolean isExpiredEmailToken(LocalDateTime expireDate) {
        if(expireDate.isBefore(LocalDateTime.now())) {
            throw new ExpiredEmailTokenException();
        } else {
            return false;
        }
    }

    public void validateEmailToken(String email, String authToken) {
        EmailAuth emailAuth = emailAuthRepository.findValidAuthByEmail(email, authToken, LocalDateTime.now()).orElseThrow(MismatchEmailTokenException::new);
        if(!isExpiredEmailToken(emailAuth.getExpireDate())) {   //EmailTokenExpiredException()
            emailAuth.useToken();
        }
    }

    public void deleteEmailAuth(String email, boolean expired) {
        EmailAuth emailAuth = emailAuthRepository.findUsedToken(email, expired).orElseThrow(MismatchEmailTokenException::new);
        emailAuthRepository.delete(emailAuth);
    }

    public void deleteUsedEmailAuth(boolean expired) {
        List<EmailAuth> usedAllToken = emailAuthRepository.findUsedAllToken(expired);
        if(!usedAllToken.isEmpty()) {
            for (EmailAuth emailAuth : usedAllToken) {
                emailAuthRepository.delete(emailAuth);
            }
        }
    }

}
