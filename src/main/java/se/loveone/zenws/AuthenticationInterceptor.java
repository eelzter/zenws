package se.loveone.zenws;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.logging.Logger;


public class AuthenticationInterceptor implements ZenInterceptor {

    //private Schema z;

    private Logger log;
    private Authenticator authenticator;

    public AuthenticationInterceptor(Authenticator authenticator) {
        log = Logger.getLogger(AuthenticationInterceptor.class.getName());
        this.authenticator = authenticator;
    }


    public Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxy) throws Throwable {
        Object o = null;
        if (notAuthorized()) {
            HttpServletResponse res = (HttpServletResponse) ThreadLocalUtil.getThreadVariable("res");
            res.setStatus(401);
            return o;
        }

        log.info("BEFORE: " + method.getName());
        long startTime = System.currentTimeMillis();
        o = methodProxy.intercept(object, method, args, methodProxy);
        long endTime = System.currentTimeMillis();
        log.info("AFTER: " + method.getName() + " time: " + (endTime - startTime));

        return o;
    }

    private boolean notAuthorized() {
        return !authenticator.isLoggedIn();
    }
}
