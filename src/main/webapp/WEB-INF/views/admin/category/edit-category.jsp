<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sửa danh mục</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1>Sửa danh mục</h1>
  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <c:choose>
    <c:when test="${fn:startsWith(category.icon, 'http')}"><c:set var="imgUrl" value="${category.icon}"/></c:when>
    <c:otherwise><c:url var="imgUrl" value="/image"><c:param name="fname" value="${category.icon}"/></c:url></c:otherwise>
  </c:choose>
  <form action="${pageContext.request.contextPath}/admin/category/edit" method="post" enctype="multipart/form-data">
    <input type="hidden" name="cateid" value="${category.cateid}">
    <label for="catename">Tên danh mục</label>
    <input type="text" id="catename" name="catename" value="<c:out value='${category.catename}'/>" required>
    <p><img class="thumb" src="${imgUrl}" alt="Ảnh hiện tại"></p>
    <label for="icon">Ảnh đại diện mới (bỏ trống để giữ ảnh cũ)</label>
    <input type="file" id="icon" name="icon" accept="image/*">
    <p>
      <button type="submit" class="btn">Lưu thay đổi</button>
      <a class="btn secondary" href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
    </p>
  </form>
</div></main>
</body>
</html>
