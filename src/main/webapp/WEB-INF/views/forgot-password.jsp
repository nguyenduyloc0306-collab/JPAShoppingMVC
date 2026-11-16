<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Quên mật khẩu</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
<main class="card auth">
  <h1>Quên mật khẩu</h1>
  <p class="muted">Nhập email đã đăng ký, chúng tôi sẽ gửi mã OTP để đặt lại mật khẩu.</p>
  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <form action="${pageContext.request.contextPath}/forgot-password" method="post">
    <label for="email">Email</label>
    <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required autofocus>
    <p><button type="submit" class="btn">Gửi mã</button></p>
  </form>
  <p class="muted"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</main>
</body>
</html>