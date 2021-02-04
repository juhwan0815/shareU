package inu.project.shareu.service;

import inu.project.shareu.domain.Member;
import inu.project.shareu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        List<SimpleGrantedAuthority> authorities = findMember.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new User(findMember.getName(), findMember.getPassword(), authorities);
    }
}
