package goodfood.repository.infra;

import goodfood.entity.user.Member;
import goodfood.repository.support.MemberRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositorySupport {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNickname(String nickname);
}
