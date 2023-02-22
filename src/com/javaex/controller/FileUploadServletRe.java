package com.javaex.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;


@WebServlet("/fileUploadRe")
public class FileUploadServletRe extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CHARSET = "utf-8";
    private static final String ATTACHES_DIR = "C:\\java_work\\java_fast\\mysite_jimin\\WebContent\\attaches";
    private static final int LIMIT_SIZE_BYTES = 1024 * 1024;
    

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
	
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding(CHARSET);
        PrintWriter out = response.getWriter();
		
        
        // 파일 설정
        File attachesDir = new File(ATTACHES_DIR);	
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setRepository(attachesDir);
        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
        
         
        String separator = null;
        String fileName = null;
        String fileName_sub = null;
        File uploadFile = null;
       
        
        
 
        try {
            List<FileItem> items = fileUpload.parseRequest(request);
            
            // 작성자 번호 
     		UserVo authUser = getAuthUser(request);
     		int userNo = authUser.getNo();
            
            BoardDao dao = new BoardDaoImpl();
            BoardVo vo = new BoardVo(); // 새로운 게시물 내용
            
            
            
            
            vo.setUserNo(userNo);
            String no = items.get(0).getString(CHARSET);  // title 값 리턴
			String title = items.get(1).getString(CHARSET);  // title 값 리턴
			String content = items.get(2).getString(CHARSET);// content 값 리턴
			
			String file_o = items.get(3).getString(CHARSET);// 변경된 file 값 리턴
			String file_sub = items.get(4).getString(CHARSET);// 원래 file 값 리턴
			String file1 = items.get(5).getString(CHARSET);// new file 값 리턴
			
			System.out.println("받아온 파일 확인 "
			        + "no : " + no
					+"/ title :" + title
					+"/ content :" + content
					+"/ file_o :" + file_o
					+"/ file_sub :" + file_sub
					+"/ file1 :" + file1);
			
			
			vo.setNo(Integer.parseInt(no));
			vo.setTitle(title);
			vo.setContent(content);
			vo.setFilename(file_o);
			vo.setFileName_sub(file_sub);
			

           System.out.println("================================================");
	   
			
            for (FileItem item : items) {
            	int a =+ 1;
                if (item.isFormField()) { //true 일반 텍스트
                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));
     
                } else {
                	//파일 
                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(), item.getName(), item.getSize());
                    
                    
                   // System.out.println("확인 getName: "+item.getName());
                    if (item.getName().equals("")) {
                    	System.out.println("기존 파일 그대로 사용");
                    	
                    }else {
                    	File file = new File(ATTACHES_DIR + "/" + file1);
                        WebUtil.delete(ATTACHES_DIR + "/" + file_o);
                        System.out.println("기존이미지삭제완료");
                    }
                    
                    
                    if (item.getSize() > 0) {
                    	System.out.println("새로운 파일 재 업로드 작업");
                        separator = File.separator;
                        int index =  item.getName().lastIndexOf(separator);
                        fileName = item.getName().substring(index  + 1);
                        uploadFile = new File(ATTACHES_DIR +  separator + fileName);
                        
                        fileName_sub = fileName;
                        if (uploadFile.exists()){
                            for(int k=0; true; k++){
                                //파일명 중복을 피하기 위한 일련 번호를 생성하여
                                //파일명으로 조합
                            	uploadFile = new File(ATTACHES_DIR, "("+k+")"+fileName);
                                //조합된 파일명이 존재하지 않는다면, 일련번호가
                                //붙은 파일명 다시 생성
                                if(!uploadFile.exists()){ //존재하지 않는 경우
                                    fileName = "("+k+")"+fileName;
                                    break;
                                }
                            }
                        }
                        
                        
                        
                        vo.setFilesize((int)item.getSize());
                        vo.setFilename(fileName);
                        vo.setFileName_sub(fileName_sub);
                        
                        item.write(uploadFile);
                        
                    } //item.getSize()
                }
                
            }//for 문
            
            System.out.println("파일업로드 중 ----------------");          
			System.out.println(vo);
			dao.update(vo);
            System.out.println("파일업로드 완료---------");
        
            System.out.printf("fileName : "+ fileName + "/fileName_sub : "+ fileName_sub + " / uploadFile : "+ uploadFile +" / separator" + separator);
       
            WebUtil.redirect(request, response, "/mysite_jimin/board?a=list");
            out.println("<h1>파일 업로드 완료</h1>");
 
 
        } catch (Exception e) {
            // 파일 업로드 처리 중 오류가 발생하는 경우
            e.printStackTrace();
            out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
        }
        
		
		
		
		
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}


}
