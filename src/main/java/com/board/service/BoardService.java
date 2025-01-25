package com.board.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.BoardEntity;
import com.board.entity.LikeEntity;

public interface BoardService {
	//게시물 목록 보기
	public Page<BoardEntity> list(int pageNum, int postNum, String keyword) throws Exception;
	
	//게시물 상세 내용 보기
	public BoardDTO view(Long seqno) throws Exception;
	
	//게시물 내용 이전 보기
	public Long pre_seqno(Long seqno,String keyword) throws Exception;
	
	//게시물 내용 다음 보기
	public Long next_seqno(Long seqno,String keyword) throws Exception;
	
	//게시물 조회수 증가
	public void hitno(Long seqno) throws Exception;
	
	//게시물 등록 하기
	public void write(BoardDTO board) throws Exception;
	
	//게시물 번호 구하기(시퀀스의 nextval을 사용)
	public Long getSeqnoWithNextval() throws Exception;
	
	//첨부파일 정보 등록하기
	public void fileInfoRegistry(FileDTO fileDTO) throws Exception;
	
	//첨부파일 목록 보기
	public List<FileDTO> fileInfoView(Long seqno) throws Exception;
	
	//게시물 삭제 시 첨부 파일 삭제를 위한 checkfile 정보 변경
	public void fileInfoUpdate(Long seqno) throws Exception;
	
	//다운로드를 위한 파일 정보 가져 오기
	public FileDTO fileInfo(Long fileseqno) throws Exception;

	//게시물 수정
	public void modify(BoardDTO board) throws Exception;	
	
	//게시물 좋아요/싫어요 갯수 수정
	public void boardLikeUpdate(BoardDTO board) throws Exception;
	
	//게시물 삭제하기
	public void delete(Long seqno) throws Exception;	
	
	//게시물 수정 시에 삭제할 파일 선택(checkfile을 X로 변경)
	public void deleteFileList(Long fileseqno) throws Exception;
	
	//좋아요/싫어요 등록여부 확인
	public LikeEntity likeCheckView(Long seqno,String email) throws Exception;
	
	//좋아요/싫어요 신규 등록
	public void likeCheckRegistry(Long seqno, String email, String mylikeCheck,
			String mydislikeCheck, String likeDate, String dislikeDate) throws Exception;
	
	//좋아요/싫어요 수정
	public void likeCheckUpdate(Long seqno, String email, String mylikeCheck,
			String mydislikeCheck, String likeDate, String dislikeDate) throws Exception;
	
	//댓글 목록 보기
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception;
	
	//댓글 등록 
	public void replyRegistry(ReplyInterface reply) throws Exception;
	
	//댓글 수정
	public void replyUpdate(ReplyInterface reply) throws Exception;
	
	//댓글 삭제 
	public void replyDelete(ReplyInterface reply) throws Exception;	

}
