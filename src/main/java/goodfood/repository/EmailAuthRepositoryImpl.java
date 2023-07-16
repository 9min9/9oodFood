package goodfood.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.user.EmailAuth;
import goodfood.repository.support.EmailAuthRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static goodfood.entity.user.QEmailAuth.emailAuth;


@Repository
@RequiredArgsConstructor
public class EmailAuthRepositoryImpl implements EmailAuthRepositorySupport {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<EmailAuth> findValidAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
        EmailAuth findAuth = queryFactory.selectFrom(emailAuth)
                .where(emailAuth.email.eq(email),
                        emailAuth.authToken.eq(authToken),
                        emailAuth.expireDate.goe(currentTime),
                        emailAuth.expired.eq(false))
                .fetchFirst();

        return Optional.ofNullable(findAuth);
    }

    @Override
    public Optional<EmailAuth> findUsedToken(String email, boolean expired) {
        EmailAuth findAuth = queryFactory.selectFrom(emailAuth)
                .where(emailAuth.email.eq(email),
                        emailAuth.expired.eq(expired))
                .fetchOne();
        return Optional.ofNullable(findAuth);
    }

    @Override
    public List<EmailAuth> findUsedAllToken(boolean expired) {
        return queryFactory.selectFrom(emailAuth)
                .where(emailAuth.expired.eq(expired))
                .fetch();
    }
}
