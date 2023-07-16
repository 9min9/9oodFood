package goodfood.service.user;

import goodfood.entity.user.Member;
import goodfood.exception.user.*;
import goodfood.repository.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean checkLogin(@AuthenticationPrincipal User user) {
        if(user == null) {
            throw new UnAuthorizedException();
        } else {
            return true;
        }
    }

    public Boolean checkDuplicatedEmail(String email) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException("이미 존재하는 이메일 입니다.");
        }
        return true;
    }

    public Boolean checkDuplicateLoginId(String loginId) {
        if(memberRepository.findByLoginId(loginId).isPresent()) {
            throw new DuplicatedLoginIdException("이미 존재하는 아이디 입니다.");
        }
        return true;
    }

    public Boolean checkMatchedLoginPassword(String loginPassword, String userPassword) {
        if(!passwordEncoder.matches(loginPassword, userPassword)) {
            throw new UnMatchedPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return true;
    }

    public Boolean checkMatchLoginIdAndPassword(String username, String password) {
        Member member = memberRepository.findByLoginId(username).orElseThrow(NotFoundUserException::new);

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnMatchedPasswordException("비밀번호 확인이 일치하지 않습니다.");
        }
        return true;
    }

}
