package inu.project.shareu.config.security;

import inu.project.shareu.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class LoginMember extends User {

    private Member member;

    public LoginMember(Member member, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.member = member;
    }

    public LoginMember(Member member,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.member = member;
    }

}
