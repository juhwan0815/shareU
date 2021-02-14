package inu.project.shareu.service;

import inu.project.shareu.advice.exception.AuthenticationEntryPointException;
import inu.project.shareu.advice.exception.MemberException;
import inu.project.shareu.config.security.LoginMember;
import inu.project.shareu.domain.Member;
import inu.project.shareu.domain.MemberStatus;
import inu.project.shareu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberPk) throws UsernameNotFoundException {

        Member findMember = memberRepository.findWithRoleById(Long.valueOf(memberPk))
                .orElseThrow(() -> new MemberException("잘못된 로그인입니다."));

        if (findMember.getMemberStatus().equals(MemberStatus.BLOCK)){
            return null;
        }

        List<SimpleGrantedAuthority> authorities = findMember.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new LoginMember(findMember.getId(), findMember.getName(), findMember.getPassword(), authorities);
    }
}
