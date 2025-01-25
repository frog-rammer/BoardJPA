package com.board.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long>{

	public List<FileEntity> findBySeqnoAndCheckfile(Long seqno, String checkfile);	
	public List<FileEntity> findBySeqno(Long seqno);
}
