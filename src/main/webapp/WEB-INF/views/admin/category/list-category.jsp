<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Quản lý danh mục</title>
</head>
<body>
<%@ include file="/WEB-INF/views/_topbar.jspf" %>
<main class="wrap"><div class="card">
  <h1>Quản lý danh mục</h1>
  <form class="toolbar" action="${pageContext.request.contextPath}/admin/category/list" method="get">
    <input type="text" name="keyword" placeholder="Tìm theo tên danh mục" value="<c:out value='${keyword}'/>" aria-label="Từ khóa">
    <button class="btn secondary" type="submit">Tìm kiếm</button>
    <a class="btn" href="${pageContext.request.contextPath}/admin/category/add">Thêm danh mục</a>
  </form>
  <div class="table-wrap">
  <table>
    <thead><tr><th>STT</th><th>Hình ảnh</th><th>Tên danh mục</th><th>Hành động</th></tr></thead>
    <tbody>
    <c:forEach items="${cateList}" var="cate" varStatus="STT">
      <c:choose>
        <c:when test="${fn:startsWith(cate.icon, 'http')}"><c:set var="imgUrl" value="${cate.icon}"/></c:when>
        <c:otherwise><c:url var="imgUrl" value="/image"><c:param name="fname" value="${cate.icon}"/></c:url></c:otherwise>
      </c:choose>
      <tr>
        <td>${STT.index + 1}</td>
        <td><img class="thumb" src="${imgUrl}" alt="<c:out value='${cate.catename}'/>"></td>
        <td><c:out value="${cate.catename}"/></td>
        <td>
          <a href="<c:url value='/admin/category/edit'><c:param name='id' value='${cate.cateid}'/></c:url>">Sửa</a> |
          <a href="<c:url value='/admin/category/delete'><c:param name='id' value='${cate.cateid}'/></c:url>"
             onclick="return confirm('Xóa danh mục này?');">Xóa</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty cateList}"><tr><td colspan="4" class="muted">Chưa có danh mục nào. Hãy thêm danh mục đầu tiên.</td></tr></c:if>
    </tbody>
  </table>
  </div>
</div></main>
</body>
</html>
