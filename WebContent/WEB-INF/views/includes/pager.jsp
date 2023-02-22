<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.javaex.vo.pageingVo" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
	
<div class="pager">
	<ul>
		<c:if test="${pageing.getNowBlock() > 1}">
			<li><a href="/mysite_jimin/board?a=list&kwd=${param.kwd}&nowPage=1">◀</a></li>
			<li><a href="/mysite_jimin/board?a=list&kwd=${param.kwd}&nowPage=${pageing.getPrev()}">◀</a></li>
		</c:if>
		<c:forEach var="i" begin="${pageing.getStartBlock()}" end="${pageing.getEndBlock()}" >
			<c:choose>
			<c:when test="${i == pageing.getNowPage()}">
				<li>${i}</li>
			</c:when>
			<c:otherwise>
				<li><a href="/mysite_jimin/board?a=list&kwd=${param.kwd}&nowPage=${i}">${i}</a></li>
			</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:if test="${pageing.getEndBlock() < pageing.getTotalPage()}">
			<li><a href="/mysite_jimin/board?a=list&kwd=${param.kwd}&nowPage=${pageing.getNext()}">▶</a></li>
			<li><a href="/mysite_jimin/board?a=list&kwd=${param.kwd}&nowPage=${pageing.getTotalPage()}">▶</a></li>
		</c:if>
	</ul>
</div>		