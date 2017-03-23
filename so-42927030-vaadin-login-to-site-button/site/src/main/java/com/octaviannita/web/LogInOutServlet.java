package com.octaviannita.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.0, Mar 23, 2017
 */
@WebServlet({"/log-me-in", "/logout", "/logoff"})
public class LogInOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        if (loggingOut(request)) {
            response.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        if (loggingOut(request)) {
            response.sendRedirect("/");
            return;
        }

        HttpSession session = request.getSession(true);

        String username = request.getParameter("username");
        if (username != null && "johnny".equalsIgnoreCase(username.trim())) {
            session.setAttribute("username", username);
            response.sendRedirect("/");
        } else {
            request.setAttribute("errorMessage", "Use 'Johnny' to log in!");
            request.getRequestDispatcher("/logon").forward(request, response);
        }
    }

    private static boolean loggingOut(HttpServletRequest request) {
        if (request == null ||
            !request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "").toLowerCase()
                    .matches("/log(?:out|off)$")) {
            return false;
        }

        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        return true;
    }
}
