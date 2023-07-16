package goodfood.controller;

import goodfood.controller.dto.admin.user.UserConfigResponse;
import goodfood.controller.dto.admin.user.UserCountResponse;
import goodfood.controller.dto.user.login.LoginRequest;
import goodfood.controller.dto.user.login.TokenInfo;
import goodfood.controller.dto.user.update.UserUpdateRequest;
import goodfood.controller.dto.user.update.UserUpdateResponse;
import goodfood.controller.dto.user.passwordreset.PasswordResetRequest;
import goodfood.controller.dto.user.signup.SignUpRequest;
import goodfood.exception.user.*;
import goodfood.service.mail.MailService;
import goodfood.service.user.MemberService;
import goodfood.service.user.UserSignService;
import goodfood.validate.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final MemberService memberService;
    private final UserSignService userSignService;
    private final MailService mailService;
    private final MessageSource messageSource;


    /** public */

    @PostMapping("/public/login/auth")
    public ResponseEntity<Object> loginProcess(@RequestBody LoginRequest request, BindingResult bindingResult) {
        try {
            System.out.println(request.getUsername());
            System.out.println(request.getPassword());

            if(!request.getUsername().isEmpty() && !request.getPassword().isEmpty()) {
                TokenInfo login = memberService.login(request.toServiceDto());
                if (login.getUserRole().equals("ROLE_ADMIN")) {
//                    return "redirect:/admin";
                }
            }
        } catch (NotFoundUserException | UnMatchedPasswordException e) {
            bindingResult.reject("user.login.error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        } finally {

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
    }


    @PostMapping("/public/duplicate/check")
    public ResponseEntity<Object> duplicateLoginIdCheckApi(@RequestBody Map<String, String> request, BindingResult bindingResult) {
        try {
            userSignService.checkDuplicateLoginId(request.get("username"));

        } catch (DuplicatedLoginIdException e) {
            bindingResult.reject("duplicate.username", "이미 존재하는 아이디입니다.");
        } finally {
            String username = request.get("username");

            if (username.length() < 5 || username.length() > 20) {
                bindingResult.reject("Size.username", "크기가 5에서 20 사이여야 합니다");
            }

            if (!Pattern.matches("^[a-z][a-z0-9]*$", username)) {
                bindingResult.reject("Pattern.username", "영어와 숫자만 입력가능합니다.");
            }

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }
        }
        return ResponseEntity.ok().build();
    }

    /** sign up */

    @PostMapping("/public/signup/confirm")
    public ResponseEntity<Object> signupSubmitApi(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        try {
            userSignService.checkDuplicateLoginId(request.getUsername());
            userSignService.checkDuplicatedEmail(request.getEmail());

        } catch (DuplicatedLoginIdException e) {
            bindingResult.rejectValue("username", "duplicate", "이미 존재하는 아이디입니다.");


        } catch (DuplicatedEmailException e) {
            bindingResult.rejectValue("email", "duplicate", "이미 등록된 이메일입니다.");
        } finally {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                bindingResult.rejectValue("confirmPassword", "unMatch", "비밀번호 확인이 일치하지 않습니다.");
            }

            if (request.getMonth() != null && request.getDay() != null) {
                if (request.getMonth() == 2 && request.getDay() > 29) {
                    bindingResult.rejectValue("day", "outOfRange", new Object[]{1, 29}, "1~29만 선택가능합니다.");
                }
            }

            if (bindingResult.hasErrors()) {
                ErrorResult errorResult = new ErrorResult(bindingResult, messageSource, Locale.getDefault());
                return ResponseEntity.badRequest().body(errorResult.getErrors());

            } else {
                memberService.signUp(request.toServiceDto());
                return ResponseEntity.ok().build();
            }
        }
    }

    @PostMapping("/public/password/reset")
    public ResponseEntity<Object> passwordResetApi(@Valid @RequestBody PasswordResetRequest request, BindingResult bindingResult) {
        try {
            memberService.checkPasswordAndPasswordConfirm(request.getChangePassword(), request.getConfirmPassword());

        } catch (NotFoundUserException e) {
            bindingResult.rejectValue("username", "notFound", null);

        } catch (UnMatchConfirmPassword e) {
            bindingResult.rejectValue("confirmPassword", "unMatch", null);
        } finally {
            if (bindingResult.hasErrors()) {
                ErrorResult errorResult = new ErrorResult(bindingResult, messageSource, Locale.getDefault());

                return ResponseEntity.badRequest().body(errorResult.getErrors());
            } else {
                memberService.passwordReset(request.toServiceDto());
                mailService.deleteUsedEmailAuth(request.getEmailAuth());

                return ResponseEntity.ok().build();
            }

        }
    }

    @PostMapping("/admin/user/delete/{id}")
    public void userDeleteApi(@PathVariable("id") Long id) {
        memberService.deleteUser(id);
    }

    /** user api */
    @PostMapping("/user/mypage/info")
    public UserUpdateResponse updateTargetApi(@AuthenticationPrincipal User user) {
        return memberService.findUpdateTarget(user.getUsername());
    }

    @PostMapping("/user/update/confirm")
    public ResponseEntity<Object> updateUserApi(@Valid @RequestBody UserUpdateRequest request, BindingResult bindingResult) {
        try {
            memberService.checkDuplicateNickname(request.getNickname());
            if (!request.getChangePassword().isEmpty()) {
                userSignService.checkMatchLoginIdAndPassword(request.getUsername(), request.getPassword());
            }
        } catch (DuplicatedNicknameException e) {
            bindingResult.rejectValue("nickname", "duplicate", null);

        } catch (UnMatchedPasswordException e) {
            bindingResult.rejectValue("password", "unMatch", null);

        } finally {
            if (!request.getChangePassword().isEmpty()) {
                if (request.getPassword().isEmpty()) {
                    bindingResult.rejectValue("password", "NotBlank", null);
                }
                if (!request.getChangePassword().equals(request.getConfirmPassword())) {
                    bindingResult.rejectValue("confirmPassword", "unMatch", null);
                }
                if (!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9!@#$%^&*]*$", request.getChangePassword())) {
                    bindingResult.rejectValue("changePassword", "Pattern", null);
                }
                if (request.getChangePassword().length() < 8 && request.getChangePassword().length() > 30) {
                    bindingResult.rejectValue("changePassword", "Size", null);
                }
            }

            if (bindingResult.hasErrors()) {
                ErrorResult errorResult = new ErrorResult(bindingResult, messageSource, Locale.getDefault());
                return ResponseEntity.badRequest().body(errorResult.getErrors());
            } else {
                memberService.updateUser(request.toUserUpdateServiceDto());
                return ResponseEntity.ok().build();
            }
        }
    }

    /** Admin Api */
    @PostMapping("/admin/user/list")
    public Page<UserConfigResponse> userConfigListApi(@RequestParam(value = "genderOpt[]", required = false) List<String> genderOpt,
                                                      @RequestParam(value = "ageOpt[]", required = false) List<String> ageOpt,
                                                      @PageableDefault(size = 5) Pageable pageable) {

        if (genderOpt == null) {
            genderOpt = Collections.emptyList();
        }

        if (ageOpt == null) {
            ageOpt = Collections.emptyList();
        }
        return memberService.getUserConfig(genderOpt, ageOpt, pageable);
    }

    @PostMapping("/admin/user/count/list")
    public UserCountResponse userConfigListCountApi(@RequestParam(value = "genderOpt[]") List<String> genderList,
                                                    @RequestParam(value = "ageOpt[]") List<String> ageList) {
        return memberService.getUserCount(genderList, ageList);
    }

    @PostMapping("/admin/user/role/update")
    public void userRoleConfigApi(@RequestParam Map<String, String> request) {
        memberService.updateUserRole(request.get("username"), request.get("changedRole"));
    }

}
