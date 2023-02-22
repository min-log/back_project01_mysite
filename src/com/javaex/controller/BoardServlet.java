package com.javaex.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;
import com.javaex.vo.pageingVo;


@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		
	
		System.out.println("board:" + actionName);

		
		if ("list".equals(actionName)) {
			request.setCharacterEncoding("utf-8");
			String kwd = request.getParameter("kwd");
			String nowpage = request.getParameter("nowPage");
			System.out.println("받아온값 kwd: " + nowpage);
			System.out.println("받아온값 nowPage: " + nowpage);
			

			if(kwd == null) kwd = "";
			if(nowpage == null) nowpage = "1";
			
			// 리스트 가져오기
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list = dao.getList(kwd,nowpage);
			pageingVo pageing = dao.getListCount(kwd,nowpage);
			
		    
			
			System.out.println(list.toString());
			
			// 리스트 화면에 보내기
			request.setAttribute("pageing", pageing);
			request.setAttribute("list", list);
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
		}else if ("search".equals(actionName)) {
			
			request.setCharacterEncoding("utf-8");
			String kwd = request.getParameter("kwd");
			String nowpage = request.getParameter("nowPage");


			if(kwd == null) kwd = "";
			if(nowpage == null) nowpage = "1";
			
			// 리스트 가져오기
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list = dao.getList(kwd,nowpage);
			pageingVo pageing = dao.getListCount(kwd,nowpage);
			
	
			System.out.println(list.toString());
			// 리스트 화면에 보내기
			request.setAttribute("pageing", pageing);
			request.setAttribute("list", list);
			String url = "/WEB-INF/views/board/list.jsp";
			WebUtil.forward(request, response, url);
		}else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			int readHit = dao.hitUpdate(no); // 게시물 조회수증가
			
			System.out.println(boardVo.toString());

			
			// 게시물 화면에 보내기
			request.setAttribute("readHit", readHit);
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
			
			
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite_jimin/board?a=list");
			
			
			
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite_jimin/board?a=list");
			}

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite_jimin/board?a=list");

		} else {
			WebUtil.redirect(request, response, "/mysite_jimin/board?a=list");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

	

}
