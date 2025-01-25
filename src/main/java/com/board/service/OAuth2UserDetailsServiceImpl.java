package com.board.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.board.dto.MemberOAuth2DTO;
import com.board.entity.MemberEntity;
import com.board.entity.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2UserDetailsServiceImpl extends DefaultOAuth2UserService{
	
	private final PasswordEncoder pwdEncoder;
	private final MemberRepository memberRepository;
	private final HttpSession session;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
		String providerId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		
		log.info("provider = {}", provider);
		log.info("providerId = {}", providerId);
		log.info("email = {}", email);
		
		oAuth2User.getAttributes().forEach((k,v)-> { 
			log.info(k + ":" + v);
		});
		
		MemberEntity member = saveSocialMember(email);
		
		//Role 값 읽어 들임...
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
		grantedAuthorities.add(grantedAuthority);
		
		MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();
		//attributes, authorities, name을 MemberOAuth2DTO에 넣어 줌. 
		memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
		memberOAuth2DTO.setAuthorities(grantedAuthorities);
		memberOAuth2DTO.setName(member.getUsername());
		
		session.setAttribute("email", email);
		session.setAttribute("username", member.getUsername());
		session.setAttribute("role", member.getRole());
		
		return memberOAuth2DTO;		
		
	}
	
	private MemberEntity saveSocialMember(String email) {
		
		//구글 회원 계정으로 로그인 한 회원의 경우 사이트 운영에 필요한 최소한의 정보를 
		//가공해서 tbl_member에 입력해야 함.
		
		Optional<MemberEntity> result = memberRepository.findById(email);
		if(result.isPresent()) {
			return result.get();
		}
		
		MemberEntity member = MemberEntity.builder()
										.email(email)
										.username("구글회원")
										.password(pwdEncoder.encode("12345"))
										.role("USER")
										.regdate(LocalDateTime.now())
										.FromSocial("Y")
										.build();
		memberRepository.save(member);
		
		return member;
	}

}
