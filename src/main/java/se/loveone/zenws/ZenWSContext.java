package se.loveone.zenws;


import javax.servlet.http.HttpServletRequest;

public class ZenWSContext {
    private static ZenWSContext instance;

    public HttpServletRequest request;

    public static ZenWSContext getInstance() {
      if(instance==null)
        instance = new ZenWSContext();

      return instance;
    }

    private ZenWSContext() {

    }
}
