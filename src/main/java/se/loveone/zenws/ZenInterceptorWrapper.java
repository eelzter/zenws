package se.loveone.zenws;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ZenInterceptorWrapper implements MethodInterceptor, ZenInterceptor {

    private ZenInterceptor interceptor;
    private MethodProxy originalMethodProxy;

    public ZenInterceptorWrapper(ZenInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if ("java.lang.Object".equals(method.getDeclaringClass().getName()))
            return methodProxy.invokeSuper(o, args);

        originalMethodProxy = methodProxy;
        return interceptor.intercept(o, method, args, this);
    }

    public Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxy) throws Throwable {
        return originalMethodProxy.invokeSuper(object, args);
    }
}
