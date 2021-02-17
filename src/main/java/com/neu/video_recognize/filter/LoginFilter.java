package com.neu.video_recognize.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "LoginFilter")
public class LoginFilter implements Filter {

    private static final Pattern[] patterns = {
            Pattern.compile("/res/*"),
            Pattern.compile("/user/login"),
            Pattern.compile("/user/register")
    };

    private boolean isExcluded(String url){
        boolean flag = false;
        for (Pattern p : patterns){
            Matcher m = p.matcher(url);
            if (!m.find()) continue;
            // 匹配成功，说明目标路径被排除，返回true
            flag = true;
            break;
        }
        return flag;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) req).getSession(false);
        String requestUrl = ((HttpServletRequest) req).getServletPath();

        if (isExcluded(requestUrl) || (session != null && session.getAttribute("user") != null)){
            chain.doFilter(req, resp);
        }else{
            ObjectMapper mapper =  new ObjectMapper();
            Map<String, Object> rs = new HashMap<>();
            rs.put("status", 0);
            rs.put("msg", "用户未登陆!");
            String response = mapper.writeValueAsString(rs);
            resp.getWriter().println(response);
            if (session != null)
                session.invalidate();
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
