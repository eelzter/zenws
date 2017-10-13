package se.loveone.zenws;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CorsFilter implements Filter {


    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;


        String karmaUrl = getProperty("karmaUrl");
        if (karmaUrl != null) {
            response.setHeader("Access-Control-Allow-Origin", karmaUrl);
        } else {
            String allowOriginUrl = getProperty("allowOriginUrl");
            response.setHeader("Access-Control-Allow-Origin", allowOriginUrl);
        }

        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
        System.out.println("Init filter!");
    }

    public void destroy() {
    }

    private static String getProperty(String property) throws IOException {
        String configFile = System.getProperties().getProperty("webapp.property.file");
        Properties p = new Properties();
        p.load(new FileInputStream(configFile));
        return p.getProperty(property);
    }

}
