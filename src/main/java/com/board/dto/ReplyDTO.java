package com.board.dto;

import java.time.LocalDateTime;

import com.board.entity.ReplyEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyDTO {
	
	private Long replyseqno;
	private Long seqno;
	private String email;
	private String replywriter;
	private String replycontent;
	private LocalDateTime replyregdate;
	
	//DTO --> Entity
	public ReplyDTO(ReplyEntity replyEntity) {
		
		this.replyseqno = replyEntity.getReplyseqno();
		this.replywriter = replyEntity.getReplywriter();
		this.replycontent = replyEntity.getReplycontent();
		this.replyregdate = replyEntity.getReplyregdate();
		this.email = replyEntity.getEmail().getEmail();
		this.seqno = replyEntity.getSeqno().getSeqno();
		
	}
	

}
