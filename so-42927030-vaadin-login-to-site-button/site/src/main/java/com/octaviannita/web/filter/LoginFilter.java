package com.octaviannita.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author <a href="mailto:Octavian.NITA@ext.ec.europa.eu">Octavian NITA</a>
 * @version $Id$
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");

        if (path.startsWith("/static/") || endsWithAny(path, "/login", "/logon", "/logout", "/logoff")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession ses = req.getSession(false);
        if (ses != null) {
            String johnny = (String) ses.getAttribute("johnny");
            if (johnny != null) {
                johnny = johnny.trim().toLowerCase();
            }
            if ("johnny".equals(johnny)) {
                ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/");
            }
        }

        ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/logon");
    }

    private static boolean endsWithAny(String path, String... suffixes) {
        if (path != null && suffixes != null) {
            for (String suffix : suffixes) {
                if (suffix != null && path.endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() {}
}
