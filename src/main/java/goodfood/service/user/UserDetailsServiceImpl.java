package goodfood.service.user;

import goodfood.entity.user.Member;
import goodfood.exception.user.NotFoundUserException;
import goodfood.repository.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(username).map(this::createUserDetails).orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
    }
    private UserDetails createUserDetails(Member member) {
        return User.builder().username(member.getLoginId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }

}
