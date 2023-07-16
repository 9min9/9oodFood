package goodfood.entity.user;

import goodfood.entity.BaseEntity;
import goodfood.entity.forum.Forum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
@Entity @Getter @Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
//@Table(name = "users")
public class Member extends BaseEntity implements UserDetails {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String loginId;

    @Column(nullable = false)
    private String email;

    private boolean emailAuth;

    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String name;

    @Column(length = 8)
    private String nickname;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(STRING)
    private UserRole role;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Forum> forumList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Favorite> favoriteList = new ArrayList<>();

    public void addForum(Forum forum) {
        this.forumList.add(forum);
    }

    public void addFavorite(Favorite favorite) {
        this.favoriteList.add(favorite);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int calcAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    public void delete() {
        forumList = null;
        favoriteList = null;
    }

    public void emailVerifiedSuccess(){
        this.emailAuth = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateRole(UserRole userRole) {
        this.role = userRole;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateGender(String gender) {
        if(gender.contains("남자") || gender.contains("여자")) {
            String korToEng = Gender.getKorToEng(gender);
            this.gender = Gender.valueOf(korToEng);
        } else {
            this.gender = Gender.valueOf(gender);
        }
    }

    public void updateBirthDate(int year, int month, int date) {
        LocalDate changedBirthDate = LocalDate.of(year, month, date);
        this.birthDate = changedBirthDate;
    }

}
