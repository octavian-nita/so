package com.octaviannita.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 23, 2017
 */
@WebFilter("/*")
public class AccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest rq = (HttpServletRequest) request;
        String path = rq.getRequestURI().substring(rq.getContextPath().length()).replaceAll("[/]+$", "").toLowerCase();

        if (!path.startsWith("/static/") && !path.matches("/log(?:in|out|on|off|-me-in)$")) {
            HttpSession sess = rq.getSession(false);
            if (sess == null || sess.getAttribute("username") == null) {
                ((HttpServletResponse) response).sendRedirect("/login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
