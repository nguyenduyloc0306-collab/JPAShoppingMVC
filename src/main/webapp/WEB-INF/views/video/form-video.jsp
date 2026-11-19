<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<c:set var="editing" value="${not empty video.videoId}"/>
<c:set var="pageTitle" value="${editing ? 'Sửa video' : 'Thêm video'}"/>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${pageTitle}"/></title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1><c:out value="${pageTitle}"/></h1>
  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>

  <c:choose>
    <c:when test="${editing}"><c:url var="formAction" value="/video/edit"><c:param name="id" value="${video.videoId}"/></c:url></c:when>
    <c:otherwise><c:url var="formAction" value="/video/add"/></c:otherwise>
  </c:choose>

  <form action="<c:out value='${formAction}'/>" method="post" enctype="multipart/form-data">
    <label for="title">Tiêu đề</label>
    <input type="text" id="title" name="title" maxlength="255" value="<c:out value='${video.title}'/>" required autofocus>

    <label for="url">Link video</label>
    <input type="text" id="url" name="url" maxlength="500" value="<c:out value='${video.url}'/>"
           placeholder="https://www.youtube.com/watch?v=...">
    <p class="hint">Link YouTube (watch, youtu.be, shorts) hoặc link trực tiếp tới file .mp4, .webm, .ogg.</p>

    <label for="videoFile">Hoặc tải file video từ máy</label>
    <input type="file" id="videoFile" name="videoFile" accept="video/*">

    <label for="description">Mô tả</label>
    <textarea id="description" name="description" maxlength="5000"><c:out value="${video.description}"/></textarea>

    <label for="cateid">Danh mục</label>
    <select id="cateid" name="cateid">
      <option value="">— Không chọn —</option>
      <c:forEach items="${categories}" var="cate">
        <option value="${cate.cateid}"
          <c:if test="${not empty video.category and video.category.cateid == cate.cateid}">selected</c:if>><c:out value="${cate.catename}"/></option>
      </c:forEach>
    </select>

    <c:if test="${editing and not empty video.poster}">
      <c:choose>
        <c:when test="${fn:startsWith(video.poster, 'http')}"><c:set var="posterUrl" value="${video.poster}"/></c:when>
        <c:otherwise><c:url var="posterUrl" value="/image"><c:param name="fname" value="${video.poster}"/></c:url></c:otherwise>
      </c:choose>
      <p><img class="thumb" src="<c:out value='${posterUrl}'/>" alt="Poster hiện tại"></p>
    </c:if>
    <label for="poster"><c:out value="${editing ? 'Poster mới (bỏ trống để giữ ảnh cũ)' : 'Poster (ảnh, tối đa 5MB)'}"/></label>
    <input type="file" id="poster" name="poster" accept="image/*">

    <label class="check"><input type="checkbox" name="active" <c:if test="${video.active}">checked</c:if>> Hiển thị công khai (lên trang chủ)</label>

    <p>
      <button type="submit" class="btn"><c:out value="${editing ? 'Lưu thay đổi' : 'Thêm video'}"/></button>
      <a class="btn secondary" href="${pageContext.request.contextPath}/video">Hủy</a>
    </p>
  </form>
</div></main>
</body>
</html>
