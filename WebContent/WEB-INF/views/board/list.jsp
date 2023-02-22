<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite_jimin/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>
</head>
<body>
	
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">	
				<form id="search_form" action="/mysite_jimin/board" method="post">
					<input type="hidden" name="a" value="search">
					<input type="hidden" name="nowPage" value="1">
					<input type="text" id="kwd" name="kwd" value="${param.kwd}">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>내용</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:if test="${list.size() == 0}">
						<tr>
							<td colspan="7" style="text-aling:center;">내용이 없습니다</td>
						</tr>
					</c:if>		
					<c:forEach items="${list}" var="vo">
						<tr>
							<td>${vo.num }</td>
							<td><a href="/mysite_jimin/board?a=read&no=${vo.no }&nowPage=${pageing.nowPage}&kwd=${param.kwd}">${vo.title}</a></td>
							<td>${vo.content}</td>
							<td>${vo.userName}</td>
							<td>${vo.hit}</td>
							<td>${vo.regDate}</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite_jimin/board?a=delete&no=${vo.no }&nowPage=7" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
			
				<c:import url="../includes/pager.jsp" />
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite_jimin/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div><!-- /container -->
</body>
</html>		
		
