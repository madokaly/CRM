package com.madoka.crm.utils.web.filter;

import com.madoka.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        String path = request.getServletPath();
        User user = (User) session.getAttribute("user");
        if("/settings/User/login.do".equals(path) || "/login.jsp".equals(path) ){
            chain.doFilter(req,resp);
        }else {
            if (user != null) {
                chain.doFilter(req,resp);
            }else{
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
    }
}
