<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đặt lại mật khẩu</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
<main class="card auth">
  <h1>Đặt lại mật khẩu</h1>
  <p class="muted">Nếu <b><c:out value="${maskedEmail}"/></b> đã đăng ký, mã OTP 6 chữ số đã được gửi đến đó.
     Mã có hiệu lực trong 5 phút.</p>

  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <c:if test="${info != null}"><p class="muted" style="color:#2e7d32"><c:out value="${info}"/></p></c:if>

  <form action="${pageContext.request.contextPath}/reset-password" method="post">
    <label for="otp">Mã OTP</label>
    <input type="text" id="otp" name="otp" maxlength="6" inputmode="numeric" pattern="\d{6}"
           autocomplete="one-time-code" required autofocus
           style="letter-spacing:8px; text-align:center; font-size:20px;">
    <label for="password">Mật khẩu mới</label>
    <input type="password" id="password" name="password" minlength="6" maxlength="72"
           autocomplete="new-password" required>
    <label for="confirm">Nhập lại mật khẩu mới</label>
    <input type="password" id="confirm" name="confirm" minlength="6" maxlength="72"
           autocomplete="new-password" required>
    <p><button type="submit" class="btn">Đặt lại mật khẩu</button></p>
  </form>

  <form action="${pageContext.request.contextPath}/reset-password" method="post">
    <input type="hidden" name="resend" value="1">
    <p class="muted">Chưa nhận được mã?
      <button type="submit" id="resendBtn" disabled
              style="background:none;border:none;padding:0;color:#1565c0;text-decoration:underline;cursor:pointer;">Gửi lại mã</button>
      <span id="countdown"></span>
    </p>
  </form>
  <p class="muted"><a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a></p>
</main>

<script>
  var left = 60, btn = document.getElementById('resendBtn'), cd = document.getElementById('countdown');
  cd.textContent = '(60s)';
  var timer = setInterval(function () {
    left--;
    if (left <= 0) { clearInterval(timer); btn.disabled = false; cd.textContent = ''; }
    else { cd.textContent = '(' + left + 's)'; }
  }, 1000);
</script>
</body>
</html>