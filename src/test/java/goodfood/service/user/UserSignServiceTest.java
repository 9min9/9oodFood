package goodfood.service.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserSignServiceTest {

    @Autowired
    UserSignService userSignService;

    @Test
    public void checkDecodePassword() {
        String loginPassword = "1234";
        String encodePassword = userSignService.encodePassword("1234");
        Boolean result = userSignService.checkMatchedLoginPassword(loginPassword, encodePassword);

        assertTrue(!loginPassword.equals(encodePassword), "암호화된 비밀번호와 평문의 비밀번호가 불일치 해야 함");
        assertTrue(result, "DB에 암호화된 비밀번호가 login 시 비밀번호와 일치해야 함");
    }
}