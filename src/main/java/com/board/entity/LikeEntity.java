package com.board.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity(name="like")
@Table(name="tbl_like")
@IdClass(LikeEntityID.class)
public class LikeEntity {
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="seqno")
	private BoardEntity seqno;
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email")
	private MemberEntity email;
	
	@Column(name="mylikecheck", length=2, nullable=true)
	private String mylikecheck;
	
	@Column(name="mydislikecheck", length=2, nullable=true)
	private String mydislikecheck;
	
	@Column(name="likedate", length=50, nullable=true)
	private String likedate;
	
	@Column(name="dislikedate", length=50, nullable=true)
	private String dislikedate;	

}
