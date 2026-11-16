<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Tạo tài khoản</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
<main class="card auth">
  <h1>Tạo tài khoản mới</h1>
  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <form action="${pageContext.request.contextPath}/register" method="post">
    <label for="username">Tài khoản</label>
    <input type="text" id="username" name="username" value="<c:out value='${param.username}'/>" required>
    <label for="fullname">Họ tên</label>
    <input type="text" id="fullname" name="fullname" value="<c:out value='${param.fullname}'/>">
    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>
    <label for="phone">Số điện thoại</label>
    <input type="text" id="phone" name="phone" value="<c:out value='${param.phone}'/>">
    <label for="password">Mật khẩu</label>
    <input type="password" id="password" name="password" autocomplete="new-password" required>
    <p><button type="submit" class="btn">Tạo tài khoản</button></p>
  </form>
  <p class="muted">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</main>
</body>
</html>
