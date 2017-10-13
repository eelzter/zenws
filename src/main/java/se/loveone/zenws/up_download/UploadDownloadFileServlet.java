package se.loveone.zenws.up_download;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

@WebServlet("/UploadDownloadFileServlet")
public class UploadDownloadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryString  = request.getQueryString();
		System.out.println("queryString:"+queryString);
		int pos = queryString.indexOf("fileName");
		System.out.println("pos:" + pos);

		//String fileName = queryString.substring(pos+"fileName".length()+1, queryString.length());

        String fileName = request.getParameter("fname");
        System.out.println("fname:" + fileName);

		String decodedfileName = URLDecoder.decode(fileName, "UTF-8");
		System.out.println("decodedfileName:" + decodedfileName);

        //fileName = URLEncoder.encode(new File(fileName).getName(), "UTF-8");


		if(fileName == null || fileName.equals("")){
			throw new ServletException("File Name can't be null or empty");
		}
        String clientDir = request.getParameter("clientDir");
        if(clientDir != null ){
            clientDir =  clientDir  + File.separator;
        }
        else
            clientDir = "";

		ServletContext ctx = getServletContext();

		File file = new File(ctx.getAttribute("FILES_DIR")+File.separator+clientDir+fileName);
		if(!file.exists()){
			throw new ServletException("File:"+file.getAbsolutePath()+" doesn't exists on server.");
		}
		System.out.println("File location on server::"+file.getAbsolutePath());

		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		response.setContentLength((int) file.length());
		//response.setHeader("Content-Disposition", "attachment; filename=\"" + decodedfileName + "\"");
		fileNameHeader(request, response, decodedfileName);

		ServletOutputStream os       = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File downloaded at client successfully");
	}

	private void fileNameHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {

		String userAgent = request.getHeader("user-agent");
		boolean isInternetExplorer = (userAgent.indexOf("MSIE") > -1);

		try {
		    byte[] fileNameBytes = fileName.getBytes((isInternetExplorer) ? ("windows-1250") : ("utf-8"));
		    String dispositionFileName = "";
		    for (byte b: fileNameBytes) dispositionFileName += (char)(b & 0xff);

		    String disposition = "attachment; filename=\"" + dispositionFileName + "\"";
		    response.setHeader("Content-disposition", disposition);
		} catch(UnsupportedEncodingException ence) {
		    // ... handle exception ...
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><head></head><body>");
		try {
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				System.out.println("FieldName="+fileItem.getFieldName());
				System.out.println("FileName="+fileItem.getName());
				System.out.println("ContentType="+fileItem.getContentType());
				System.out.println("Size in bytes="+fileItem.getSize());

				ServletContext ctx = getServletContext();

				File file = new File(ctx.getAttribute("FILES_DIR")+File.separator+ URLEncoder.encode(fileItem.getName(), "UTF-8"));
				System.out.println("Absolute Path at server="+file.getAbsolutePath());
				fileItem.write(file);
				out.write("File "+fileItem.getName()+ " uploaded successfully.");
				out.write("<br>");
				out.write("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");
			}
		} catch (FileUploadException e) {
			out.write("Exception in uploading file.");
		} catch (Exception e) {
			out.write("Exception in uploading file.");
		}
		out.write("</body></html>");
	}

}
