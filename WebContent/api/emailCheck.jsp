<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.Enumeration"%>

<%
      // 요청파라미터로  중복할 체크할 아이디
      String id = request.getParameter("email");  // id 중복체크
      
      Enumeration e = request.getParameterNames();
      while ( e.hasMoreElements() ){
        String name = (String) e.nextElement();
        String[] values = request.getParameterValues(name);   
        for (String value : values) {
          System.out.println("name=" + name + ",value=" + value);
        }   
      }
      
      
      int cnt = 0;
      //   cnt=1  아이디 사용중이다.  
      //       0  아이디 사용가능하다.
      String sql = " select count(*) cnt " + 
                   "   from users " + 
                   "  where email = ?";
      
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      String url = "jdbc:oracle:thin:@localhost:1521:XE";
      String user = "webdb";
      String pass = "1234";
      
      try{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection(url, user, pass);
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        rs = pstmt.executeQuery();
        if(rs.next()){
          cnt = rs.getInt("cnt");  // 1      0  
        }
        
      }catch(Exception ex){
         ex.printStackTrace();
      }finally{
        try{
          if(rs != null)   rs.close();
          if(pstmt != null) pstmt.close();
          if(conn != null) conn.close();
        }catch(Exception ex){
          ex.printStackTrace();
        }
      }
      
      if(cnt == 1){
        out.print("true");
      }else{
        out.print("false");
      }
      
      
%>