package se.loveone.zenws;

import java.lang.reflect.Method;
import java.util.logging.Logger;


public class TransactionDemarcationInterceptor implements ZenInterceptor {

    private Logger log;
    private Transactable database;


    public TransactionDemarcationInterceptor(Transactable database) {
        log = Logger.getAnonymousLogger();
        this.database = database;
        ThreadLocalUtil.setThreadVariable("nesting", 0);
    }

    public Object intercept(Object object, Method method, Object[] args, ZenInterceptor methodProxy) throws Throwable {
        if(ThreadLocalUtil.getThreadVariable("nesting") == null) {
            ThreadLocalUtil.setThreadVariable("nesting", 0);
        }
        Object o = null;

        ThreadLocalUtil.setThreadVariable("nesting",((Integer) ThreadLocalUtil.getThreadVariable("nesting")) +1);
        Logger.getAnonymousLogger().info("BEFORE: " + method.getName() + " nesting: " +ThreadLocalUtil.getThreadVariable("nesting").toString());
        long startTime = System.currentTimeMillis();
        try {
            o = methodProxy.intercept(object, method, args, methodProxy);
        } catch (Exception e) {
            log.info("ROLLING BACK!");
            ThreadLocalUtil.setThreadVariable("nesting", 0);
            database.rollback();
            e.printStackTrace();
            throw e;
        }
        long endTime = System.currentTimeMillis();
        Logger.getAnonymousLogger().info("AFTER: " + method.getName() + " nesting: " + ThreadLocalUtil.getThreadVariable("nesting").toString() + " time: " +(endTime - startTime));

        ThreadLocalUtil.setThreadVariable("nesting",((Integer) ThreadLocalUtil.getThreadVariable("nesting")) -1);
        if ((Integer) ThreadLocalUtil.getThreadVariable("nesting") == 0) {
            log.info("COMMITTING!");
            database.commit();
        }
        return o;
    }
}
