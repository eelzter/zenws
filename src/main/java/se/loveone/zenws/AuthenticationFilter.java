package se.loveone.zenws;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest))
            throw new ServletException("Unauthorized access, unable to forward to login page");
        else {
            String uri = ((HttpServletRequest) request).getRequestURI();

            ThreadLocalUtil.setThreadVariable("req", request);
            ThreadLocalUtil.setThreadVariable("res", response);
            //TODO:This should only be done if the user has right to proceed!
            chain.doFilter(request, response);
            ThreadLocalUtil.destroy();
        }
    }

    public void destroy() {
    }
}

