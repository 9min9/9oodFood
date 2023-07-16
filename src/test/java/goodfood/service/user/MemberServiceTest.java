package goodfood.service.user;

import goodfood.controller.dto.user.login.LoginRequest;
import goodfood.controller.dto.user.signup.SignUpRequest;
import goodfood.controller.dto.user.login.TokenInfo;
import goodfood.entity.user.Gender;
import goodfood.entity.user.Member;
import goodfood.entity.user.UserRole;
import goodfood.exception.user.DuplicatedEmailException;
import goodfood.exception.user.DuplicatedLoginIdException;
import goodfood.exception.user.NotFoundUserException;
import goodfood.repository.infra.MemberRepository;
import goodfood.service.dto.user.LoginServiceDto;
import goodfood.service.dto.user.UserServiceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;

import javax.transaction.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserSignService signService;

    private SignUpRequest setRequest(String loginId, String email, String password, String confirmPassword, String name, String gender, int age) {
        SignUpRequest request = new SignUpRequest();
        request.setUsername(loginId);
        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        request.setName(name);
        request.setGender(gender);
//        request.setYear(1997);
//        request.setMonth(2);
//        request.setDay(26);

        return request;
    }

    private void createTestUser() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testId");
        request.setEmail("test@test.com");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        request.setName("테스트유저");
        request.setGender("남");
//        request.setYear("1997");
//        request.setMonth("02");
//        request.setDay("26");

        UserServiceDto serviceDto = request.toServiceDto();
        Long saveUserId = memberService.signUp(serviceDto);
    }

    @Test
    @DisplayName("회원 가입")
    public void createUserTest() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testId");
        request.setEmail("gming0226@gmail.com");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        request.setName("구민규");
        request.setGender("남");
//        request.setYear(1997);
//        request.setMonth(02);
//        request.setDay(26);


        UserServiceDto serviceDto = request.toServiceDto();
        Long saveUserId = memberService.signUp(serviceDto);
        Member findMember = memberRepository.findById(saveUserId).get();

        assertEquals(UserRole.USER, findMember.getRole(), "User 생성 시 기본 역할은 USER");
        assertEquals(findMember.getName(),  findMember.getNickname(), "회원 가입 시 기본 닉네임은 이름과 동일");
        System.out.println(findMember.getPassword());

    }

    @Test
    @DisplayName("회원가입 아이디, 이메일 중복")
    public void checkDuplicatedTest() {
        createTestUser();

        assertThrows(DuplicatedLoginIdException.class, () -> {
            SignUpRequest request = setRequest("testId", "test2@test.com", "password", "password", "테스트 유저", "남", 26);
            UserServiceDto userServiceDto = request.toServiceDto();
            memberService.signUp(userServiceDto);
        }, "로그인 아이디가 중복될 시 DuplicatedLoginIdException이 발생해야 함");


        assertThrows(DuplicatedEmailException.class, ()-> {
            SignUpRequest request = setRequest("testId2", "test@test.com", "test1234", "test1234", "테스트유저", "남", 25);
            UserServiceDto serviceDto = request.toServiceDto();
            Long saveUserId = memberService.signUp(serviceDto);
        }, "이메일이 중복될 시 DuplicationEmailException이 발생해야 함");
    }

    @Test
    @DisplayName("로그인 아이디 일치하지 않음")
    public void checkLoginIdTest() {
        createTestUser();
        assertThrows(NotFoundUserException.class, () -> {
            LoginRequest request = new LoginRequest();
            request.setUsername("abcdef123");
            request.setPassword("1234");
            LoginServiceDto serviceDto = request.toServiceDto();
            memberService.login(serviceDto);
        }, "로그인 아이디가 일치하지 않으면 Exception 발생");
    }

    @Test
    @DisplayName("로그인 비밀번호 일치하지 않음")
    public void checkLoginPasswordTest() {
        createTestUser();

        assertThrows(BadCredentialsException.class, () -> {
            LoginRequest request = new LoginRequest();
            request.setUsername("testId");
            request.setPassword("asdf1234");
            LoginServiceDto serviceDto = request.toServiceDto();
            TokenInfo tokenInfo = memberService.login(serviceDto);

        },"로그인 시 비밀번호가 일치하지 않으면 Exception 발생.");
    }

    @Test
    @DisplayName("로그인 성공")
    public void loginTest() {
        createTestUser();

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("fk1gks23");
        LoginServiceDto serviceDto = request.toServiceDto();
        TokenInfo tokenInfo = memberService.login(serviceDto);

        assertTrue(!tokenInfo.getAccessToken().isEmpty(), "로그인이 성공할 시 tokenInfo의 AccessToken이 발급");
        assertTrue(!tokenInfo.getRefreshToken().isEmpty(), "로그인이 성공할 시 tokenInfo의 RefreshToken이 발급");
    }

    @Test
    @DisplayName("로그 아웃")
    public void logoutTest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("fk1gks23");
        LoginServiceDto serviceDto = request.toServiceDto();
        TokenInfo tokenInfo = memberService.login(serviceDto);

        assertTrue(!tokenInfo.getAccessToken().isEmpty(), "로그인이 성공할 시 tokenInfo의 AccessToken이 발급");
        assertTrue(!tokenInfo.getRefreshToken().isEmpty(), "로그인이 성공할 시 tokenInfo의 RefreshToken이 발급");

    }

    @Test
    @DisplayName("회원 관리 카운트 테스트")
    public void userConfigCountingTest() {
        List<String> genderOpt = new ArrayList<>();
        genderOpt.add("MALE");
        genderOpt.add("FEMALE");
        genderOpt.add("ALL");

        List<String> ageOpt = new ArrayList<>();
        ageOpt.add("10대");
        ageOpt.add("20대");
        ageOpt.add("30대");
        ageOpt.add("40대");
        ageOpt.add("50대이상");
        ageOpt.add("전체");

        HashMap<String, List<Long>> result = new HashMap<>();
        for (String gender : genderOpt) {
            List<Long> countList = new ArrayList<>();
            for (String age : ageOpt) {
                Long userCount = memberRepository.findUserCount(gender, age);
                countList.add(userCount);
            }
            result.put(Gender.getKorToEng(gender), countList);
        }

        System.out.println(result);
    }

    @Test
    @DisplayName("회원 삭제")
    public void deleteTest() {
        Member user3 = memberRepository.findByLoginId("user3").get();
        memberRepository.delete(user3);

        Optional<Member> deleteUser = memberRepository.findByLoginId("user3");
        assertTrue(deleteUser.isEmpty(), "유저가 삭제됨");

    }


}