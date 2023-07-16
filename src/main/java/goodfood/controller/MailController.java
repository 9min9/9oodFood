package goodfood.controller;

import goodfood.controller.dto.mail.SendEmailRequest;
import goodfood.controller.dto.mail.ValidMailAuthTokenRequest;
import goodfood.exception.mail.ExpiredEmailTokenException;
import goodfood.exception.mail.MismatchEmailTokenException;
import goodfood.exception.mail.UnMatchEmailAndUsername;
import goodfood.exception.user.DuplicatedEmailException;
import goodfood.exception.user.NotFoundUserException;
import goodfood.service.mail.MailService;
import goodfood.service.user.UserSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserSignService userSignService;

    /** public */

    @PostMapping("/public/mail/validate")
    public ResponseEntity<Object> validateEmailTokenApi(@Valid @RequestBody ValidMailAuthTokenRequest request, BindingResult bindingResult) {
        try {
            mailService.validateEmailToken(request.getEmail(), request.getToken());

        } catch (MismatchEmailTokenException e) {
            bindingResult.rejectValue("token", "misMatch.token", "인증번호가 잘못 입력되었습니다.");

        } catch (ExpiredEmailTokenException e) {
            bindingResult.rejectValue("token", "expired.token", "인증번호가 만료되었습니다.");
        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            } else {
                return ResponseEntity.ok().build();

            }
        }
    }

    /** sign up */

    @PostMapping("/public/signup/mail/send")
    public ResponseEntity<Object> signUpMailValidateApi(@RequestBody @Valid SendEmailRequest request, BindingResult bindingResult) {
        try {
            userSignService.checkDuplicatedEmail(request.getEmail());
        } catch (DuplicatedEmailException e) {
            bindingResult.rejectValue("email", "duplicate.email", "중복된 이메일 입니다.");

        } finally {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            } else {
                String email = request.getEmail();
                String message = request.getMessage();
                String authToken = mailService.createAuthToken(6);
                mailService.saveEmailToken(email, authToken);
                mailService.sendEmail(email, authToken, message);
                return ResponseEntity.ok().build();
            }
        }
    }

    /** password reset */

    @PostMapping("/public/password/reset/mail/send")
    public ResponseEntity<Object> passwordResetMailSendApi(@Valid @RequestBody Map<String, String> request, BindingResult bindingResult) {
        try {
            mailService.checkLoginIdAndEmail(request.get("loginId"), request.get("email"));
        } catch (NotFoundUserException e) {
            bindingResult.reject("notFound.username", "존재하지 않는 아이디입니다.");

        } catch (UnMatchEmailAndUsername e) {
            bindingResult.reject("unMatch.email", "아이디와 이메일이 일치하지 않습니다.");

        } finally {
            if (bindingResult.hasErrors()) {
                Map<String, Object> errors = new HashMap<>();
                List<ObjectError> allErrors = bindingResult.getAllErrors();

                for (ObjectError allError : allErrors) {
                    errors.put("code", allError.getCode());
                    errors.put("message", allError.getDefaultMessage());
                }

                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            } else {
                String authToken = mailService.createAuthToken(6);
                mailService.saveEmailToken(request.get("email"), authToken);
                mailService.sendEmail(request.get("email"), authToken, "9oodFood 비밀번호 찾기 인증 메일");
                return ResponseEntity.ok().build();
            }
        }
    }

}
