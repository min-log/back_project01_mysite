package com.javaex.vo;

public class BoardVo {
	private int num;
	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
	//file 변수 추가
	private String filename;
	private String fileName_sub;
	private int filesize;
	
	
	

	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}
	
	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}
	
	 
//	리스트 추가 페이지  -> file 추가 객체 생성
//	회원명 (가입해야 작성가능)
//	제목
//	내용
//	파일 명
//	파일 사이즈
	public BoardVo(String title, String content, int userNo, String filename,int filesize) {
		this(title,content,userNo);
		this.filename = filename;
		this.filesize = filesize;
	}

	
	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	// 리스트 객체
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
	}
	
	//인설트 및  내용확인
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName,String filename , String fileName_sub) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
		this.filename = filename;
		this.fileName_sub = fileName_sub;
	}
	
	public BoardVo(int num, int no, String title, String content, int hit, String regDate, int userNo, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.num = num;
		this.content = content;
	}
	
	
	
	public String getFileName_sub() {
		return fileName_sub;
	}

	public void setFileName_sub(String fileName_sub) {
		this.fileName_sub = fileName_sub;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", filename=" + filename + ", filesize="
				+ filesize + "]";
	}

		
	

}
