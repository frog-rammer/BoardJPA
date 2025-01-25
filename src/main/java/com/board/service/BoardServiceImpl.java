package com.board.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.board.dto.BoardDTO;
import com.board.dto.FileDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.BoardEntity;
import com.board.entity.FileEntity;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.repository.BoardRepository;
import com.board.entity.repository.FileRepository;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

	private final BoardRepository boardRepository;
	private final FileRepository fileRepository;
	private final LikeRepository likeRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	
	//게시물 목록 보기
	@Override
	public Page<BoardEntity> list(int pageNum, int postNum, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"seqno"));
		return boardRepository.findByTitleContainingOrWriterContainingOrContentContaining(keyword, keyword, keyword, pageRequest);
	}

	//게시물 내용 보기
	@Override
	public BoardDTO view(Long seqno) throws Exception {
		return boardRepository.findById(seqno).map(view -> new BoardDTO(view)).get();
	}
	
	//게시물 내용 이전 보기
	@Override
	public Long pre_seqno(Long seqno,String keyword) throws Exception {
		return boardRepository.pre_seqno(seqno, keyword, keyword, keyword)==null?0:boardRepository.pre_seqno(seqno, keyword, keyword, keyword);
	}

	//게시물 내용 다음 보기
	@Override
	public Long next_seqno(Long seqno,String keyword) throws Exception {
		return boardRepository.next_seqno(seqno, keyword, keyword, keyword)==null?0:boardRepository.next_seqno(seqno, keyword, keyword, keyword);
	}

	//게시물 내용 조회수 증가
	@Override
	public void hitno(Long seqno) throws Exception {
		boardRepository.hitno(seqno);
	}

	//게시물 등록 하기
	@Override
	public void write(BoardDTO board) throws Exception {
		board.setRegdate(LocalDateTime.now());
		boardRepository.save(board.dtoToEntity(board));
	}
	
	//게시물 번호 구하기(시퀀스의 nextval을 사용)
	@Override
	public Long getSeqnoWithNextval() throws Exception{
		return boardRepository.getSeqnoWithNextval();
	}
	
	//첨부파일 정보 등록하기
	@Override
	public void fileInfoRegistry(FileDTO fileDTO) throws Exception{
		fileRepository.save(fileDTO.dtoToEntity(fileDTO));
	}
	
	//첨부파일 목록 보기
	@Override
	public List<FileDTO> fileInfoView(Long seqno) throws Exception{
		List<FileDTO> fileDTOs = new ArrayList<>();
		fileRepository.findBySeqnoAndCheckfile(seqno, "Y").stream().forEach(list-> fileDTOs.add(new FileDTO(list)));
		return fileDTOs;
	}
	
	//게시물 삭제 시 첨부 파일 삭제를 위한 checkfile 정보 변경
	@Override
	public void fileInfoUpdate(Long seqno) throws Exception {
		fileRepository.findBySeqno(seqno).stream().forEach(file -> {
			file.setCheckfile("N");
			fileRepository.save(file);
		});
	}
	
	//다운로드를 위한 파일 정보 가져 오기
	@Override
	public FileDTO fileInfo(Long fileseqno) throws Exception {
		return fileRepository.findById(fileseqno).map(file -> new FileDTO(file)).get();	
	}

	//게시물 수정 하기
	@Override
	public void modify(BoardDTO board) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getSeqno()).get();
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		boardRepository.save(boardEntity);
	}
	
	//게시물 좋아요/싫어요 갯수 수정
	@Override
	public void boardLikeUpdate(BoardDTO board) throws Exception{
		BoardEntity boardEntity = boardRepository.findById(board.getSeqno()).get();
		boardEntity.setLikecnt(board.getLikecnt());
		boardEntity.setDislikecnt(board.getDislikecnt());
		boardRepository.save(boardEntity);
	}

	//게시물 삭제 하기
	@Override
	public void delete(Long seqno) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		boardRepository.delete(boardEntity);	
	}
	
	//게시물 수정 시에 삭제할 파일 선택(checkfile을 X로 변경)
	@Override
	public void deleteFileList(Long fileseqno) throws Exception{
		FileEntity fileEntity = fileRepository.findById(fileseqno).get();
		fileEntity.setCheckfile("N");
		fileRepository.save(fileEntity);	
	}
	
	//좋아요/싫어요 등록여부 확인
	@Override
	public LikeEntity likeCheckView(Long seqno,String email) throws Exception {

		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		return likeRepository.findBySeqnoAndEmail(boardEntity,memberEntity);
	}
	
	//좋아요/싫어요 신규 등록
	@Override
	public void likeCheckRegistry(Long seqno, String email, String mylikeCheck,
			String mydislikeCheck, String likeDate, String dislikeDate)	throws Exception{
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		LikeEntity likeEntity = LikeEntity.builder()
										.seqno(boardEntity)
										.email(memberEntity)
										.mylikecheck(mylikeCheck)
										.mydislikecheck(mydislikeCheck)
										.likedate(likeDate)
										.dislikedate(dislikeDate)
										.build();
		likeRepository.save(likeEntity);		
	}
	
	//좋아요/싫어요 수정
	@Override
	public void likeCheckUpdate(Long seqno, String email, String mylikeCheck,
			String mydislikeCheck, String likeDate, String dislikeDate) throws Exception{
		BoardEntity boardEntity = boardRepository.findById(seqno).get();
		MemberEntity memberEntity = memberRepository.findById(email).get();
		LikeEntity likeEntity = likeRepository.findBySeqnoAndEmail(boardEntity, memberEntity);
		likeEntity.setMylikecheck(mylikeCheck);
		likeEntity.setMydislikecheck(mydislikeCheck);
		likeEntity.setLikedate(likeDate);	
		likeEntity.setDislikedate(dislikeDate);	
		likeRepository.save(likeEntity);
	}
	
	//댓글 목록 보기
	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception{
		return replyRepository.replyView(reply.getSeqno());
	}
	
	//댓글 등록 
	@Override
	public void replyRegistry(ReplyInterface reply) throws Exception {
		BoardEntity boardEntity = boardRepository.findById(reply.getSeqno()).get();
		MemberEntity memberEntity = memberRepository.findById(reply.getEmail()).get();
		
		ReplyEntity replyEntity = ReplyEntity.builder()
											.seqno(boardEntity)
											.email(memberEntity)
											.replywriter(reply.getReplywriter())
											.replycontent(reply.getReplycontent())
											.replyregdate(LocalDateTime.now())
											.build();
		replyRepository.save(replyEntity);
	}
	
	//댓글 수정
	@Override
	public void replyUpdate(ReplyInterface reply) throws Exception{
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyEntity.setReplycontent(reply.getReplycontent());	
		replyRepository.save(replyEntity);		
	}	
	
	//댓글 삭제
	@Override
	public void replyDelete(ReplyInterface reply) throws Exception{
		ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno()).get();
		replyRepository.delete(replyEntity);	
	}

}
