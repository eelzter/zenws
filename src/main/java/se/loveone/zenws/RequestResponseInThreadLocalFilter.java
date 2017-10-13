package se.loveone.zenws;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestResponseInThreadLocalFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest))
            throw new ServletException("Unauthorized access, unable to forward to login page");
        else {
            ThreadLocalUtil.setThreadVariable("req", request);
            ThreadLocalUtil.setThreadVariable("res", response);
            chain.doFilter(request, response);
            ThreadLocalUtil.destroy();
        }
    }

    public void destroy() {
    }
}

