package goodfood.service.user;

import goodfood.controller.dto.admin.user.UserConfigResponse;
import goodfood.controller.dto.admin.user.UserCountResponse;
import goodfood.controller.dto.user.login.TokenInfo;
import goodfood.controller.dto.user.update.UserUpdateResponse;
import goodfood.entity.user.Gender;
import goodfood.entity.user.Member;
import goodfood.entity.user.UserRole;
import goodfood.exception.user.DuplicatedNicknameException;
import goodfood.exception.user.NotFoundUserException;
import goodfood.exception.user.UnMatchConfirmPassword;
import goodfood.exception.user.UnMatchedPasswordException;
import goodfood.repository.infra.MemberRepository;
import goodfood.service.dto.user.LoginServiceDto;
import goodfood.service.dto.user.PasswordResetServiceDto;
import goodfood.service.dto.user.UserServiceDto;
import goodfood.service.dto.user.UserUpdateServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserSignService userSignService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    /** Create */

    @Transactional
    public Long signUp(UserServiceDto serviceDto) {
        serviceDto.setPassword(userSignService.encodePassword(serviceDto.getPassword()));
        Member member = serviceDto.toEntity();
        Member saveMember = memberRepository.save(member);
        return saveMember.getId();
    }

    /** Read */


    public Member findByUsername(String username) {
        Optional<Member> findUser = memberRepository.findByLoginId(username);
        if (findUser.isEmpty()) {
            throw new NotFoundUserException();
        } else {
            return findUser.get();
        }
    }

    public Page<UserConfigResponse> getUserConfig(List<String> genderOpt, List<String> ageOpt, Pageable pageable) {
        List<Gender> genderList = toEngGenderList(genderOpt);
        List<Integer> integerAgeList = ageOpt.stream().map(str -> Integer.parseInt(str.substring(0, 2))).toList();
        Page<Member> findUser = memberRepository.findUserList(genderList, integerAgeList, pageable);
        return findUser.map(member -> new UserConfigResponse().toDto(member));
    }

    public UserCountResponse getUserCount(List<String> genderOpt, List<String> ageOpt) {
        HashMap<String, List<Long>> result = new HashMap<>();

        for (String gender : genderOpt) {
            List<Long> countList = new ArrayList<>();
            for (String age : ageOpt) {
                Long userCount = memberRepository.findUserCount(gender, age);
                countList.add(userCount);
            }
            result.put(gender, countList);
        }
        return new UserCountResponse().toDto(result);
    }

    public UserUpdateResponse findUpdateTarget(String loginId) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);

        if (byLoginId.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        } else {
            Member member = byLoginId.get();
            return new UserUpdateResponse().toDto(member);
        }
    }

    @Transactional
    public TokenInfo login(LoginServiceDto serviceDto) {
        Optional<Member> findUser = memberRepository.findByLoginId(serviceDto.getLoginId());

        if (findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        } else if (!userSignService.checkMatchedLoginPassword(serviceDto.getPassword(), findUser.get().getPassword())) {
            throw new UnMatchedPasswordException("아이디 혹은 비밀번호가 잘못되었습니다.");
        } else {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(serviceDto.getLoginId(), serviceDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            return tokenInfo;
        }
    }

    /** Update */
    @Transactional
    public void updateUserRole(String loginId, String role) {
        Optional<Member> findUser = memberRepository.findByLoginId(loginId);
        if (findUser.isEmpty()) {
            throw new NotFoundUserException("유저가 존재하지 않습니다.");
        } else {
            Member member = findUser.get();
            member.updateRole(UserRole.valueOf(role));
        }
    }

    @Transactional
    public Member updateUser(UserUpdateServiceDto serviceDto) {
        Member member = memberRepository.findByLoginId(serviceDto.getLoginId()).orElseThrow(NotFoundUserException::new);

        if (serviceDto.getChangedPassword() != null && !serviceDto.getChangedPassword().isEmpty()) {
            if (userSignService.checkMatchedLoginPassword(serviceDto.getOriginPassword(), member.getPassword()) &&
                    serviceDto.getChangedPassword().equals(serviceDto.getConfirmPassword())) {
                member.updatePassword(userSignService.encodePassword(serviceDto.getChangedPassword()));
            }
        }

        if (serviceDto.isCheckedEmailAuth()) {
            member.emailVerifiedSuccess();
        }

        if (serviceDto.getNickname() != null && !serviceDto.getNickname().isEmpty()) {
            member.updateNickname(serviceDto.getNickname());
        }

        if (serviceDto.getName() != null && !serviceDto.getName().isEmpty()) {
            member.updateName(serviceDto.getName());
        }

        if (serviceDto.getGender() != null && !serviceDto.getGender().isEmpty()) {
            member.updateGender(serviceDto.getGender());
        }

        if (serviceDto.getYear() != null && !serviceDto.getYear().isEmpty() &&
                serviceDto.getMonth() != null && !serviceDto.getMonth().isEmpty() &&
                serviceDto.getDay() != null && !serviceDto.getDay().isEmpty()) {

            member.updateBirthDate(Integer.parseInt(serviceDto.getYear()), Integer.parseInt(serviceDto.getMonth()), Integer.parseInt(serviceDto.getDay()));
        }
        return member;
    }

    public boolean checkPasswordAndPasswordConfirm(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            throw new UnMatchConfirmPassword("비밀번호 확인이 일치하지 않습니다.");
        }
    }

    public boolean checkDuplicateNickname(String nickname) {
        Optional<Member> findUser = memberRepository.findByNickname(nickname);

        if (findUser.isPresent()) {
            if(!findUser.get().getNickname().equals(nickname)) {
                throw new DuplicatedNicknameException("이미 등록된 닉네임입니다.");
            }
        }
        return false;
    }


    public void passwordReset(PasswordResetServiceDto serviceDto) {
        Member member = memberRepository.findByLoginId(serviceDto.getLoginId()).orElseThrow(NotFoundUserException::new);
        if (serviceDto.getChangePassword() != null && !serviceDto.getChangePassword().isEmpty() ||
                serviceDto.getConfirmPassword() != null && !serviceDto.getConfirmPassword().isEmpty()) {
            if (checkPasswordAndPasswordConfirm(serviceDto.getChangePassword(), serviceDto.getConfirmPassword())) {
                member.updatePassword(userSignService.encodePassword(serviceDto.getChangePassword()));
            }
        }
    }

    /** Delete */

    public void deleteUser(Long id) {
        Optional<Member> findUser = memberRepository.findById(id);
        if (findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        } else {
            Member member = findUser.get();
            memberRepository.delete(member);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    private List<Gender> toEngGenderList(List<String> genderOpt) {
        List<Gender> genderList = new ArrayList<>();
        for (String gender : genderOpt) {
            String korToEng = Gender.getKorToEng(gender);
            genderList.add(Gender.valueOf(korToEng));
        }
        return genderList;
    }


}
