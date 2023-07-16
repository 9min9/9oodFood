package goodfood.repository.support;

import goodfood.entity.user.Gender;
import goodfood.entity.user.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositorySupport {
    Page<Member> findUserList(List<Gender> genderOpt, List<Integer> ageOpt, Pageable pageable);

    Long findUserCount(String gender, String age);
}
