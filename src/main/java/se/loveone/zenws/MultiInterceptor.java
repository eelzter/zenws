package se.loveone.zenws;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MultiInterceptor implements MethodInterceptor, ZenInterceptor {

    private List<ZenInterceptor> interceptors;
    private short index;
    private MethodProxy originalMethodProxy;

    public MultiInterceptor() {
        interceptors = new ArrayList<ZenInterceptor>();
    }

    public void addInterceptor(ZenInterceptor mi) {
        interceptors.add(mi);
    }


    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if ("java.lang.Object".equals(method.getDeclaringClass().getName()))
            return methodProxy.invokeSuper(o, args);

        originalMethodProxy = methodProxy;
        index=1;
        return interceptors.get(0).intercept(o, method, args, this);
    }

    public Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxy) throws Throwable {
        ZenInterceptor zi = interceptors.get(index);
        index++;
        if(index < interceptors.size())
            return zi.intercept(object, method, args, this);
        else
            return zi.intercept(object, method, args, new MethodProxyZenInterceptor(originalMethodProxy));
    }
}
