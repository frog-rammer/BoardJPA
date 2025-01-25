package com.board.dto;

import java.time.LocalDateTime;

import com.board.entity.BoardEntity;
import com.board.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

	private Long seqno;
	private String writer;
	private String title;
	private String content;
	private MemberEntity email;
	private LocalDateTime regdate;
	private int hitno;
	private int likecnt;
	private int dislikecnt;
	
	//Entity --> DTO 이동
	public BoardDTO(BoardEntity boardEntity) {
		this.email = boardEntity.getEmail();
		this.seqno = boardEntity.getSeqno();
		this.writer = boardEntity.getWriter();
		this.title = boardEntity.getTitle();
		this.content = boardEntity.getContent();
		this.regdate = boardEntity.getRegdate();		
		this.hitno = boardEntity.getHitno();
		this.likecnt = boardEntity.getLikecnt();
		this.dislikecnt = boardEntity.getDislikecnt();
	}
	
	//DTO --> Entity 이동
	public BoardEntity dtoToEntity(BoardDTO dto) {
		
		BoardEntity boardEntity = BoardEntity.builder()
											.email(dto.getEmail())
											.seqno(dto.getSeqno())
											.writer(dto.getWriter())
											.title(dto.getTitle())
											.regdate(dto.getRegdate())
											.content(dto.getContent())
											.hitno(dto.getHitno())
											.likecnt(dto.getLikecnt())
											.dislikecnt(dto.getDislikecnt())
											.build();
		return boardEntity;
	}
	
	
	

}
