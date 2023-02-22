package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.UserDao;
import com.javaex.dao.UserDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user2")
public class UserServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
			
		System.out.println("UserServlet2.doGet() 호출");
    request.setCharacterEncoding("utf-8");

    String actionName = request.getParameter("a");
    
    System.out.println("a -> " +actionName );
    
    //회원가입폼
    if("joinform".equals(actionName)) {
      /*
      RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
      rd.forward(request, response);
      */
      
      WebUtil.forward(request, response, "/WEB-INF/views/user/joinform.jsp");
    }else if("join".equals(actionName)) {
      String name = request.getParameter("name");
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      String gender = request.getParameter("gender");
      UserVo vo = new UserVo(name,email,password,gender);
      
      UserDao dao = new UserDaoImpl();
      dao.insert(vo);
      
      WebUtil.forward(request, response, "/WEB-INF/views/user/joinsuccess.jsp");
    }else if("loginform".equals(actionName)) {
      WebUtil.forward(request, response, "/WEB-INF/views/user/loginform.jsp");
    }

    
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
