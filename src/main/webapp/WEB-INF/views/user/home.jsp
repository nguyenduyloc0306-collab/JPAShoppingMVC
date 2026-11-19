<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Trang chủ</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1>Trang chủ</h1>
  <c:if test="${sessionScope.account != null}">
    <p>Xin chào, <c:out value="${sessionScope.account.fullName}"/>.</p>
  </c:if>

  <h2>Video mới nhất</h2>
  <c:choose>
    <c:when test="${empty latestVideos}">
      <p class="muted">Chưa có video nào.</p>
    </c:when>
    <c:otherwise>
      <div class="vgrid">
        <c:forEach items="${latestVideos}" var="v">
          <%@ include file="/WEB-INF/views/_video-card.jspf" %>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</div></main>
</body>
</html>
