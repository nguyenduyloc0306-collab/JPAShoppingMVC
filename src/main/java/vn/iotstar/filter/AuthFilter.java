package vn.iotstar.filter;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;

/** /admin/* chỉ cho roleid = 1, /manager/* chỉ cho roleid = 2. */
@WebFilter(urlPatterns = { "/admin/*", "/manager/*" })
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String path = req.getServletPath();
        boolean allowed = (path.startsWith("/admin") && user.getRoleid() == 1)
                || (path.startsWith("/manager") && user.getRoleid() == 2);
        if (!allowed) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        chain.doFilter(request, response);
    }
}
