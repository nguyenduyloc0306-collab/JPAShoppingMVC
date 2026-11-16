<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Xác thực tài khoản</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
<main class="card auth">
  <h1>Xác thực tài khoản</h1>
  <p class="muted">Mã OTP gồm 6 chữ số đã được gửi đến
     <b><c:out value="${maskedEmail}"/></b>. Mã có hiệu lực trong 5 phút.</p>

  <c:if test="${alert != null}"><p class="alert"><c:out value="${alert}"/></p></c:if>
  <c:if test="${info != null}"><p class="muted" style="color:#2e7d32"><c:out value="${info}"/></p></c:if>

  <form action="${pageContext.request.contextPath}/verify-otp" method="post">
    <label for="otp">Mã OTP</label>
    <input type="text" id="otp" name="otp" maxlength="6" inputmode="numeric"
           pattern="\d{6}" autocomplete="one-time-code" autofocus required
           style="letter-spacing:8px; text-align:center; font-size:20px;">
    <p><button type="submit" class="btn">Xác nhận</button></p>
  </form>

  <form action="${pageContext.request.contextPath}/resend-otp" method="post">
    <p class="muted">Chưa nhận được mã?
      <button type="submit" id="resendBtn" disabled
              style="background:none;border:none;padding:0;color:#1565c0;text-decoration:underline;cursor:pointer;">Gửi lại mã</button>
      <span id="countdown"></span>
    </p>
  </form>
</main>

<script>
  // Chờ 60 giây mới cho bấm "Gửi lại mã" (chỉ là giao diện, không thay thế kiểm tra phía server)
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