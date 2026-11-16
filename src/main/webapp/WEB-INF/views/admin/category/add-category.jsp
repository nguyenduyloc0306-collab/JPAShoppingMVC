<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Thêm danh mục</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1>Thêm danh mục</h1>
  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <form action="${pageContext.request.contextPath}/admin/category/add" method="post" enctype="multipart/form-data">
    <label for="catename">Tên danh mục</label>
    <input type="text" id="catename" name="catename" required>
    <label for="icon">Ảnh đại diện</label>
    <input type="file" id="icon" name="icon" accept="image/*">
    <p>
      <button type="submit" class="btn">Thêm danh mục</button>
      <a class="btn secondary" href="${pageContext.request.contextPath}/admin/category/list">Hủy</a>
    </p>
  </form>
</div></main>
</body>
</html>
