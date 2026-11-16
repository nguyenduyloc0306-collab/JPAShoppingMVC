<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Đăng nhập</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
  <main class="card auth">
    <h1>Đăng nhập</h1>

    <c:if test="${param.activated == '1'}">
      <p style="color:green">Kích hoạt thành công, hãy đăng nhập.</p>
    </c:if>
    <c:if test="${param.registered == '1'}">
      <p class="alert ok">Đăng ký thành công. Hãy đăng nhập.</p>
    </c:if>
    <c:if test="${param.reset == '1'}">
      <p class="muted" style="color:#2e7d32">Đặt lại mật khẩu thành công, hãy đăng nhập.</p>
    </c:if>
    <c:if test="${alert != null}">
      <p class="alert"><c:out value="${alert}" /></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
      <label for="username">Tài khoản</label>
      <input type="text" id="username" name="username" autocomplete="username" required>
      <label for="password">Mật khẩu</label>
      <input type="password" id="password" name="password" autocomplete="current-password" required>
      <label class="check"><input type="checkbox" name="remember"> Nhớ tôi</label>
      <button type="submit" class="btn">Đăng nhập</button>
    </form>

    <p class="muted"><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></p>
    <p class="muted">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
  </main>
</body>
</html>