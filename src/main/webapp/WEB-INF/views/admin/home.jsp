<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Trang quản trị</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1>Trang quản trị</h1>
  <p>Bạn đang đăng nhập với quyền admin.</p>
  <p><a class="btn" href="${pageContext.request.contextPath}/admin/category/list">Quản lý danh mục</a></p>
</div></main>
</body>
</html>
