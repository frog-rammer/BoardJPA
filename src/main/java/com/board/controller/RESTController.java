package com.board.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.board.dto.BoardDTO;
import com.board.dto.ReplyDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.repository.BoardRepository;
import com.board.entity.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RESTController {
	
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	
	@GetMapping("/rest/list")
	public List<BoardDTO> getList(){
		
		List<BoardDTO> boardDTOs = new ArrayList<>();
		boardRepository.findAll().stream().forEach(list->boardDTOs.add(new BoardDTO(list)));
		return boardDTOs;
	}
	
	@GetMapping("/rest/reply/{seqno}")
	public List<ReplyInterface> getReply(@PathVariable("seqno") Long seqno){
		return replyRepository.replyView(seqno);
		//return replyRepository.replyView1(seqno);
	}
	
}
