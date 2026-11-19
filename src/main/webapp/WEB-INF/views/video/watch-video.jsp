<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${video.title}"/></title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">

  <c:choose>
    <c:when test="${not empty video.youtubeId}">
      <div class="player">
        <iframe src="https://www.youtube.com/embed/<c:out value='${video.youtubeId}'/>"
                title="<c:out value='${video.title}'/>"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
      </div>
    </c:when>
    <c:when test="${video.directMedia}">
      <c:choose>
        <c:when test="${not empty video.poster and fn:startsWith(video.poster, 'http')}"><c:set var="posterUrl" value="${video.poster}"/></c:when>
        <c:when test="${not empty video.poster}"><c:url var="posterUrl" value="/image"><c:param name="fname" value="${video.poster}"/></c:url></c:when>
        <c:otherwise><c:set var="posterUrl" value=""/></c:otherwise>
      </c:choose>
      <div class="player">
        <video controls preload="metadata" <c:if test="${not empty posterUrl}">poster="<c:out value='${posterUrl}'/>"</c:if>>
          <source src="<c:out value='${video.url}'/>">
          Trình duyệt của bạn không hỗ trợ phát video.
        </video>
      </div>
    </c:when>
    <c:otherwise>
      <p class="alert">Không phát được video này (link không hợp lệ).</p>
    </c:otherwise>
  </c:choose>

  <h1 class="vtitle">
    <c:out value="${video.title}"/>
    <c:if test="${not video.active}"><span class="badge">Đang ẩn</span></c:if>
  </h1>
  <p class="muted">
    <c:if test="${video.user != null}">Đăng bởi <strong><c:out value="${video.user.fullName}"/></strong> · </c:if>${video.views} lượt xem<c:if test="${not empty video.createdText}"> · ${video.createdText}</c:if><c:if test="${video.category != null}"> · <c:out value="${video.category.catename}"/></c:if>
  </p>

  <c:if test="${not empty video.description}">
    <div class="vdesc"><c:out value="${video.description}"/></div>
  </c:if>

  <p>
    <a class="btn secondary" href="${pageContext.request.contextPath}/user/home">← Trang chủ</a>
    <c:if test="${owner}">
      <a class="btn secondary" href="${pageContext.request.contextPath}/video">← Video của tôi</a>
      <a class="btn" href="<c:url value='/video/edit'><c:param name='id' value='${video.videoId}'/></c:url>">Sửa</a>
    </c:if>
  </p>
  <c:if test="${owner}">
    <form action="${pageContext.request.contextPath}/video/delete" method="post"
          onsubmit="return confirm('Xóa video này?');">
      <input type="hidden" name="id" value="<c:out value='${video.videoId}'/>">
      <button type="submit" class="linkbtn">Xóa video này</button>
    </form>
  </c:if>
</div></main>
</body>
</html>
