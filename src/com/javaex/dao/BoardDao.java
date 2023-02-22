package com.javaex.dao;

import java.util.List;
import com.javaex.vo.BoardVo;
import com.javaex.vo.pageingVo;

public interface BoardDao {

	public List<BoardVo> getList(String kwd,String nowPage);  // 게시물 전체 목록 조회 > 각페이지 넘버 값을 받아서 리스트 출력
	public pageingVo getListCount(String kwd,String _nowPage);
	
	
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	
	public int update(BoardVo vo);   // 게시물 수정
	public int hitUpdate(int no);   // 조회수증가

	
}
