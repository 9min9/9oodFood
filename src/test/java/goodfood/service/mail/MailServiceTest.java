package goodfood.service.mail;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailServiceTest {

    @Test
    void test() {
        String randomStr =
                UUID.randomUUID().toString().replaceAll("[^a-zA-Z^0-9]", "").substring(0, 6);
//                UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);

        System.out.println(randomStr);

    }

}