package goodfood.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.user.Gender;
import goodfood.entity.user.Member;
import goodfood.repository.support.MemberRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static goodfood.entity.user.QMember.member;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long findUserCount(String gender, String age) {
        return queryFactory
                .select(member.count()).from(member)
                .where(eqGender(gender),
                        eqAge(age)).fetchOne();
    }

    @Override
    public Page<Member> findUserList(List<Gender> genderOpt, List<Integer> ageOpt, Pageable pageable) {
        List<Member> content = getUserList(genderOpt, ageOpt, pageable);
        Long count = countUserList(genderOpt, ageOpt);

        return new PageImpl<>(content, pageable, count);
    }

    private Long countUserList(List<Gender> genderOpt, List<Integer> ageOpt) {
        DatePath<LocalDate> birthDate = member.birthDate;

        return queryFactory.select(member.count()).from(member)
                .where(sortGender(genderOpt), sortAge(ageOpt))
                .fetchOne();
    }

    private List<Member> getUserList(List<Gender> genderOpt, List<Integer> ageOpt, Pageable pageable) {
        return queryFactory.select(member).from(member)
                .where(sortGender(genderOpt),
                        sortAge(ageOpt))
                .fetch();
    }


    private BooleanExpression sortAge(List<Integer> ageRangeList) {
        if(ageRangeList.isEmpty()) {
            return null;
        }

        List<BooleanExpression> expressions = new ArrayList<>();
        for (int i = 0; i < ageRangeList.size(); i++) {
            int lowerBound = ageRangeList.get(i);
            System.out.println("sortAge : " +lowerBound);

            if(lowerBound == 50 ) {
                expressions.add(member.birthDate.lt(LocalDate.now().minusYears(lowerBound)));
            } else {
                int upperBound = ageRangeList.get(i) + 9;
                expressions.add(member.birthDate.goe(LocalDate.now().minusYears(upperBound))
                        .and(member.birthDate.lt(LocalDate.now().minusYears(lowerBound))));
            }
        }
        return Expressions.anyOf(expressions.toArray(new BooleanExpression[expressions.size()]));
    }

    private BooleanExpression sortGender(List<Gender> genderOpt) {
        if (CollectionUtils.isEmpty(genderOpt)) {
            return null;
        } else {
            return member.gender.in(genderOpt);
        }
    }

    private BooleanExpression eqGender(String gender) {
        if(gender.equals("ALL") || gender.isEmpty() || gender.isBlank() ) {
            return null;
        } else {
//            return member.gender.eq(Gender.valueOf(Gender.getKorToEng(gender)));
            return member.gender.eq(Gender.valueOf(gender));
        }
    }

    private BooleanExpression eqAge(String strAge) {
        List<BooleanExpression> expressions = new ArrayList<>();
        if(strAge.equals("전체") || strAge.isEmpty() || strAge.isBlank()) {
            return null;
        } else {
            int lowerBound = Integer.parseInt(strAge.substring(0, 2));

            if(lowerBound == 50 ) {
                expressions.add(member.birthDate.lt(LocalDate.now().minusYears(lowerBound)));
            } else {
                int upperBound = Integer.parseInt(strAge.substring(0, 2)) + 9;
                expressions.add(member.birthDate.goe(LocalDate.now().minusYears(upperBound))
                        .and(member.birthDate.lt(LocalDate.now().minusYears(lowerBound))));
            }
        }
        return Expressions.anyOf(expressions.toArray(new BooleanExpression[expressions.size()]));
    }

    private void calcAge(List<Integer> ageRangeList) {
        for (Integer age : ageRangeList) {
            int lowerBound = age;

            if(lowerBound == 50) {
                LocalDate.now().minusYears(lowerBound);
            } else {
                int upperBound = age + 9;

            }
        }

    }


}
