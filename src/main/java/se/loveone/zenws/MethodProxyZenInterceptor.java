package se.loveone.zenws;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MethodProxyZenInterceptor implements ZenInterceptor {

    private MethodProxy methodProxy;

    public MethodProxyZenInterceptor(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }

    public Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxyArg) throws Throwable{
        return methodProxy.invokeSuper(object, args);
    }
}
