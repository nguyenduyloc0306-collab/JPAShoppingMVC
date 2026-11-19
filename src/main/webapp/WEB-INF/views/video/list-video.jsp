<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Video của tôi</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <div class="page-head">
    <h1>Video của tôi</h1>
    <a class="btn" href="${pageContext.request.contextPath}/video/add">Thêm video</a>
  </div>

  <c:choose>
    <c:when test="${param.msg == 'added'}"><p class="alert ok">Đã thêm video.</p></c:when>
    <c:when test="${param.msg == 'updated'}"><p class="alert ok">Đã lưu thay đổi.</p></c:when>
    <c:when test="${param.msg == 'deleted'}"><p class="alert ok">Đã xóa video.</p></c:when>
  </c:choose>

  <c:choose>
    <c:when test="${total == 0}">
      <p class="muted">Bạn chưa có video nào. Hãy thêm video đầu tiên.</p>
    </c:when>
    <c:otherwise>
      <p class="muted">Tổng ${total} video · Trang ${currentPage}/${totalPages}</p>
      <c:set var="showActions" value="true"/>
      <div class="vgrid">
        <c:forEach items="${videos}" var="v">
          <%@ include file="/WEB-INF/views/_video-card.jspf" %>
        </c:forEach>
      </div>

      <c:if test="${totalPages > 1}">
        <nav class="pager" aria-label="Phân trang">
          <c:choose>
            <c:when test="${currentPage > 1}">
              <a href="<c:url value='/video'><c:param name='page' value='${currentPage - 1}'/></c:url>">« Trước</a>
            </c:when>
            <c:otherwise><span class="disabled">« Trước</span></c:otherwise>
          </c:choose>

          <c:forEach begin="${currentPage - 2 < 1 ? 1 : currentPage - 2}"
                     end="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" var="p">
            <c:choose>
              <c:when test="${p == currentPage}"><span class="current" aria-current="page">${p}</span></c:when>
              <c:otherwise>
                <a href="<c:url value='/video'><c:param name='page' value='${p}'/></c:url>">${p}</a>
              </c:otherwise>
            </c:choose>
          </c:forEach>

          <c:choose>
            <c:when test="${currentPage < totalPages}">
              <a href="<c:url value='/video'><c:param name='page' value='${currentPage + 1}'/></c:url>">Sau »</a>
            </c:when>
            <c:otherwise><span class="disabled">Sau »</span></c:otherwise>
          </c:choose>
        </nav>
      </c:if>
    </c:otherwise>
  </c:choose>
</div></main>
</body>
</html>
