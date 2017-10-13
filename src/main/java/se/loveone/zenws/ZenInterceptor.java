package se.loveone.zenws;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public interface ZenInterceptor {
    Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxy) throws Throwable;
}
