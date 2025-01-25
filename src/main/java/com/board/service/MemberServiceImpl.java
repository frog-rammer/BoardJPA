package com.board.service;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.dto.MemberDTO;
import com.board.entity.AddressEntity;
import com.board.entity.MemberEntity;
import com.board.entity.repository.AddressRepository;
import com.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
	private final BCryptPasswordEncoder pwdEncoder;
	
	//회원 가입
	@Override
	public void memberInfoRegistry(MemberDTO member) {
		member.setRegdate(LocalDateTime.now());
		member.setLastpwdate(LocalDateTime.now());
		member.setPwcheck(1);
		member.setRole("USER");
		member.setFromSocial("N");	
		memberRepository.save(member.dtoToEntity(member));	
	}
	
	//회원 정보 가져 오기
	@Override
	public MemberDTO memberInfo(String email) {
		return memberRepository.findById(email).map(member -> new MemberDTO(member)).get();
	}
	
	//아이디 중복 확인
	public int idCheck(String email) {
		return memberRepository.findById(email).isEmpty()?0:1;
	};
	
	//패스워드 수정
	public void memberPasswordModify(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setPassword(pwdEncoder.encode(member.getPassword()));
		memberRepository.save(memberEntity);	
	}
	
	//마지막 로그인 날짜 등록 하기
	@Override
	public void lastlogindateUpdate(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogindate(member.getLastlogindate());
		memberRepository.save(memberEntity);
	}
	
	//마지막 로그아웃 날짜 등록 하기
	@Override
	public void lastlogoutdateUpdate(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setLastlogoutdate(member.getLastlogindate());	
		memberRepository.save(memberEntity);
	}
	
	//아이디 찾기
	public String searchID(MemberDTO member) {
		return memberRepository.findByUsernameAndTelno(member.getUsername(),member.getTelno())
					.map(m-> m.getEmail()).orElse("ID_NOT_FOUND");
	}
	
	//임시 패스워드 생성
	@Override
	public String tempPasswordMaker() {
		//숫자 + 영문대소문자 7자리 임시패스워드 생성
		StringBuffer tempPW = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 7; i++) {
		    int rIndex = rnd.nextInt(3);
		    switch (rIndex) {
		    case 0:
		        // a-z : 아스키코드 97~122
		    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z : 아스키코드 65~122
		    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    case 2:
		        // 0-9
		    	tempPW.append((rnd.nextInt(10)));
		        break;
		    }
		}		
		return tempPW.toString();	
	}

	
	//주소 검색
	@Override
	public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch) {
		PageRequest pageRequest = PageRequest.of(pageNum-1, postNum, Sort.by(Direction.ASC,"zipcode"));
		return addressRepository.findByRoadContainingOrBuildingContaining(addrSearch, addrSearch, pageRequest);
	}

}
