package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.pageingVo;

public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  

	
	// 키워드와 페이지에 따른 리스트 갯수 얻기
	public pageingVo getListCount(String kwd,String nowPage_) {
		pageingVo pageing = null;
		int nowPage;
		if(nowPage_ == null || nowPage_.equals("")) {
			nowPage = Integer.parseInt("1");
		} else {
			nowPage = Integer.parseInt(nowPage_);
		}
		
		if(kwd == null) kwd = "";
		System.out.println("nowPage :" + nowPage);
	
		int totalRecord = 0; // 총페이지
		
		
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		String kwdup = "%" + kwd + "%";
		System.out.println("키워드와 페이지에 따른 리스트 갯수 얻기 kwdup : " + kwdup);
		System.out.println("키워드와 페이지에 따른 리스트 갯수 얻기 nowPage_ 1: " + nowPage_);
		
		
		try {
			
			conn = getConnection();
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT count(b.NO)" +  // 총개수
					"FROM (" + 
					"		SELECT ROWNUM num,T1.NO,T1.TITLE,T1.HIT,T1.REG_DATE ,T1.CONTENT,T1.USER_NO,T2.NAME " + 
					"		FROM (SELECT * " + 
					"			  FROM BOARD " + 
					"			  ORDER BY REG_DATE desc) T1  " + 
					"		INNER JOIN USERS T2  " + 
					"		ON (T1.USER_NO = T2.NO)  " + 
					"		WHERE T1.TITLE LIKE ?" + 
					"		OR T1.CONTENT LIKE ? " + 
					"		OR T1.REG_DATE LIKE ?" + 
					"		OR T2.NAME LIKE ? ) b";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, kwdup);
          pstmt.setString(2, kwdup);
          pstmt.setString(3, kwdup);
          pstmt.setString(4, kwdup);
          
			rs = pstmt.executeQuery();
			// 4.결과처리
			//int index = 0 ;
			if (rs.next()) {
				totalRecord = rs.getInt(1);
			}
			System.out.println(totalRecord);
			System.out.println(nowPage);
			pageing= new pageingVo(totalRecord, nowPage);
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		
		
		
		
		return pageing;

	}

	
  
	
	
	// 검색어를 받아서 필터링 한 내용    
	public List<BoardVo> getList(String kwd,String nowPage) {
		System.out.println("리스트 구하기 kwd ?: " + kwd);
		System.out.println("리스트 구하기  nowPage_ ?: " + nowPage);
		if(kwd == null) kwd = "";
		if(nowPage == null) nowPage = "1";
		
		pageingVo pageing = getListCount(kwd ,nowPage);
		int start = pageing.getStart();
		int end = pageing.getEnd();
		
		System.out.println("리스트 구하기 kwd : " + kwd);
		System.out.println("리스트 구하기  nowPage_ : " + nowPage);
		
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		String kwdup = "%" + kwd + "%";
		
		try {
			conn = getConnection();
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT b.*" + 
					"FROM (" + 
					"		SELECT ROWNUM num,T1.NO,T1.TITLE,T1.HIT,T1.REG_DATE ,T1.CONTENT,T1.USER_NO,T2.NAME  " + 
					"		FROM (SELECT * " + 
					"				FROM BOARD " + 
					"				ORDER BY REG_DATE desc)T1  " + 
					"		INNER JOIN USERS T2  " + 
					"		ON (T1.USER_NO = T2.NO)  " + 
					"		WHERE T1.TITLE LIKE ? " + 
					"		OR T1.CONTENT LIKE  ? " + 
					"		OR T1.REG_DATE LIKE ? " + 
					"		OR T2.NAME LIKE ?) b " + 
					"WHERE num BETWEEN ? AND ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, kwdup);
            pstmt.setString(2, kwdup);
            pstmt.setString(3, kwdup);
            pstmt.setString(4, kwdup);
			pstmt.setInt(5, start);
			pstmt.setInt(6, end);
            
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				
				int num = rs.getInt("num");
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				
				// 날짜 포멧 변경 
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(regDate); 
				String regDate1 = formatter.format(date);
				
				// 타이틀 & 컨텐츠 글자수 제한
//				 if(title.length() > 10 || content.length() > 10) { //문자열 10이상일경우
//					 title.substring(4);
//					 content.substring(4);
//				 }
				
				
				BoardVo vo = new BoardVo(num, no, title,content, hit, regDate1, userNo, userName);
				
				

				
				list.add(vo);
			}
			
		} catch (SQLException | ParseException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return list;

	}

	
	
	
	
	
	
	//게시물 얻기 
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, b.filename, b.fileName_sub ,u.name "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String filename = rs.getString("filename");
				String fileName_sub = rs.getString("fileName_sub");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName, filename ,fileName_sub);
			}
			 
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		System.out.println("잘들어갔나 확인 :" + boardVo);
		return boardVo;

	}
	//게시물 저장 
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		
		
	
		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
	      System.out.println("vo.title : ["+vo.getTitle()+"]");
	      System.out.println("vo.content : ["+vo.getContent()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행	      
			String query = "insert into board \r\n" + 
					"values (SEQ_BOARD_NO.nextval, ?, ?, 0, sysdate, ?,?,?,?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			
			//파일 데이터 추가 
			pstmt.setString(4, vo.getFilename());
			pstmt.setInt(5, vo.getFilesize());
			pstmt.setString(6, vo.getFileName_sub());
			
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	//게시물 삭제
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		
		//	실제 파일제거
	    String ATTACHES_DIR = "C:\\java_work\\java_fast\\mysite_jimin\\WebContent\\attaches";
	    BoardVo boardVo = null;
	     

		try {
			
			conn = getConnection();

			
			//	실제 파일제거
			String query1 = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, b.filename, b.fileName_sub ,u.name "
				     + "from board b, users u "
				     + "where b.user_no = u.no "
				     + "and b.no = ?";
		
			pstmt1 = conn.prepareStatement(query1);
			pstmt1.setInt(1, no);
			
			rs = pstmt1.executeQuery();
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String filename = rs.getString("filename");
				String fileName_sub = rs.getString("fileName_sub");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName, filename ,fileName_sub);
			}
			String file_o = boardVo.getFilename();
			WebUtil.delete(ATTACHES_DIR+ "/" + file_o);
			
			// DB내용 제거
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);
		
			
			count = pstmt.executeUpdate();


			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	//게시물 업데이트
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?,content = ?,filename = ?,fileName_sub = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getFilename());
			pstmt.setString(4, vo.getFileName_sub());
			pstmt.setInt(5, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	 // 게시물 hit 수 증가 
	public int hitUpdate(int no) {
		
		Connection conn = null;
		PreparedStatement pstat = null;
		
		int count = 0;
		String query ="UPDATE BOARD \n " + 
				"SET HIT = HIT + 1 \n " + 
				"WHERE NO = ?";
		
		try {
			conn = getConnection();
			pstat = conn.prepareStatement(query);
			pstat.setInt(1, no);
			
			count = pstat.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return count;
	};
}
