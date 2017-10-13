package se.loveone.zenws;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.CallbackHelper;
import net.sf.cglib.proxy.Enhancer;


public class Wrapper {


  public static Object wrap(Class clazz) throws Exception {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    return enhancer.create();
  }

  public static Object wrap(Class clazz, Transactable database) throws Exception {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback(new ZenInterceptorWrapper(new TransactionDemarcationInterceptor(database)));
    return enhancer.create();
  }

  public static Object wrapSecure(Class clazz, Authenticator authenticator) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(clazz);
    enhancer.setCallback(new ZenInterceptorWrapper(new AuthenticationInterceptor(authenticator)));
    return enhancer.create();
  }

    public static Object wrapSecure(Class clazz, Authenticator authenticator, Transactable database) {
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(clazz);
      MultiInterceptor multiInterceptor = new MultiInterceptor();
      enhancer.setCallback(multiInterceptor);
      multiInterceptor.addInterceptor(new AuthenticationInterceptor(authenticator));
      multiInterceptor.addInterceptor(new TransactionDemarcationInterceptor(database));
      return enhancer.create();
    }
}
