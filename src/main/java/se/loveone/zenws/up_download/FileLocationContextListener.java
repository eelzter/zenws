package se.loveone.zenws.up_download;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

@WebListener
public class FileLocationContextListener implements ServletContextListener {

    private Logger log = Logger.getAnonymousLogger();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	String rootPath = System.getProperty("catalina.home");
    	ServletContext ctx = servletContextEvent.getServletContext();
        Properties p = new Properties();
        String configFile="";
        try {
            configFile = System.getProperties().getProperty("webapp.property.file");
            if(configFile == null)
                return;

            p.load(new FileInputStream(configFile));

        } catch (IOException e) {
            log.warning(configFile+" failed to load!!");
            e.printStackTrace();
        }
        //String relativePath = ctx.getInitParameter("tempfile.dir");
        String absolutePath = File.separator+p.getProperty("webRoot")+File.separator+p.getProperty("uploadFileDir");
        //String absolutePath = System.getProperty("user.home")+File.separator+p.getProperty("uploadFileDir");
    	//File file = new File(rootPath + File.separator + relativePath);

    	File file = new File(absolutePath);
        if(!file.exists())
            file.mkdirs();

        System.out.println("File Directory created to be used for storing files");
    	ctx.setAttribute("FILES_DIR_FILE", file);
//    	ctx.setAttribute("FILES_DIR", rootPath + File.separator + relativePath);
        ctx.setAttribute("FILES_DIR", absolutePath);
    }

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		//do cleanup if needed
	}
	
}
