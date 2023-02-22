package com.javaex.vo;

public class pageingVo {
	//기본 셋팅 값
	private int numPerPage=10; // 페이지당 리스트 수 
	private int pagePerBlock=5; //블럭당 페이지 수 
	
	// 초기화
	private int totalRecord=0; //전체 리스트 수 
	private int totalPage=0; //전체 페이지 수
	private int totalBlock=0;  //전체 블럭수 

	private int nowPage = 1; // 현재페이지
	private int nowBlock = 1;  // 현재블럭
	private int prev = 0;
	private int next = 0;
	
	//한 블럭에서 시작 페이지와  끝 페이지
	private int startBlock = 1;
	private int endBlock = 1;
	
	//게시물 리스트
	private int start=0; //디비의 select 시작번호
	private int end=10; //시작번호로 부터 가져올 select 갯수
	

	public pageingVo() {}
	public pageingVo(int totalRecord,int nowPage) {
		// 변경된 현제 페이지와 , 전체 리스트 수 
		this.nowPage = nowPage;
		this.totalRecord = totalRecord; 
		
		//현재블럭 계산 = 현재페이지 2 / 블럭당 페이지수 5
		this.nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock);	
				
		
		//리스트 관련 개수 
		//전체페이지수
		this.totalPage = (int)Math.ceil((double)totalRecord / numPerPage);  
		// 전체 페이지 수 /블럭당 페이지수 = 전체블럭계산 
		this.totalBlock = (int)Math.ceil((double)totalPage / pagePerBlock);  //전체블럭계산 30 2
				
		
		// page
		this.endBlock = (int)Math.ceil((double) nowPage / pagePerBlock)* pagePerBlock; // 2 / 10	
		this.startBlock = endBlock - pagePerBlock + 1; // 2 / 10
		

		//현제 페이지 넘버 * 리스트 갯수  - 리스트개수  2 * 10 - 10 + 1  
		this.start = (nowPage * numPerPage)-numPerPage + 1; // 1-10 11-20
		this.end = nowPage * numPerPage;
		
		//block 이동 
		this.prev = getStartBlock() - getPagePerBlock();
		this.next = getNowBlock() * getPagePerBlock() + 1;
		
		

		
		
		
		//마지막 페이지 수가 노출 가능한 페이지 수 보다 작을때
		if( this.endBlock > this.totalPage) this.endBlock = this.totalPage;
	}

	

	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public int getPrev() {
		return prev;
	}
	public void setPrev(int prev) {
		this.prev = prev;
	}
	public int getEndBlock() {
		return endBlock;
	}
	public void setEndBlock(int endBlock) {
		this.endBlock = endBlock;
	}
	public int getStartBlock() {
		return startBlock;
	}
	public void setStartBlock(int startBlock) {
		this.startBlock = startBlock;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public int getPagePerBlock() {
		return pagePerBlock;
	}
	public void setPagePerBlock(int pagePerBlock) {
		this.pagePerBlock = pagePerBlock;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalBlock() {
		return totalBlock;
	}
	public void setTotalBlock(int totalBlock) {
		this.totalBlock = totalBlock;
	}
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public int getNowBlock() {
		return nowBlock;
	}
	public void setNowBlock(int nowBlock) {
		this.nowBlock = nowBlock;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}


	
	
}
