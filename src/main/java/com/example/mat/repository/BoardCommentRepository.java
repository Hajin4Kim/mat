package com.example.mat.repository;

import com.example.mat.entity.won.BoardComment;
import com.example.mat.entity.won.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    /**
     * 특정 게시글의 댓글 목록 조회 (최신 순)
     * 
     * @param board 댓글이 속한 게시글
     * @return 댓글 목록
     */
    List<BoardComment> findByBoardOrderByRegDateDesc(Board board);

    // /**
    // * 특정 부모 댓글의 대댓글 조회
    // *
    // * @param parent 부모 댓글
    // * @return 대댓글 목록
    // */
    // List<BoardComment> findByParentOrderByRegDateAsc(BoardComment parent);
}