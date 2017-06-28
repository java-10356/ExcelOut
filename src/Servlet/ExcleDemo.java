package Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.offcn.DaoImpl.StudentDaoImpl;

import po.Student;

public class ExcleDemo extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//设置响应头格式及编码
		response.setContentType("text/html; charset=utf-8");
		//第一步 ：将上传的文件流 写入到软件 存储目录
		File file = (File) uploadFile(request,response);
		String lastm = file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().length());
		
		if(lastm.equals("XlS")||lastm.equals("xls")){
			//第二步：读取表格内容 组成list
			List<HashMap<String, String>> stulist = importXlS(file);
			//第三步：把list循环插入数据库
			StudentDaoImpl.insert(stulist);
		}else if(lastm.equals("XLSX")||lastm.equals("xlsx")){
			//对应不同版本Excle
			List<HashMap<String, String>> stulist = importXlSX(file);
			StudentDaoImpl.insert(stulist);
		}
		
	
	}

	
	private Object uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String webroot = this.getServletContext().getRealPath("/");
		File temppath = new File(webroot + "fileuploadtemp");//临时存储目录
		File uploadpath = new File(webroot + "fileupload");//存储目录
		if (!temppath.exists()) {
			temppath.mkdirs();
		}
		if (!uploadpath.exists()) {
			uploadpath.mkdirs();
		}
        //声明diskfileitemfactory工厂类，用于在指的磁盘上设置一个临时目录
		DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,temppath);
		//声明ServletFileUpoload，接收上面的临时目录
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置上传文件最大值
		upload.setFileSizeMax(1024 * 1024 * 100);
        
		try {
			//解析request，获得上传的数据结果集
			List<FileItem> fileItems = upload.parseRequest(request);
			//迭代遍历结果集
			Iterator<FileItem> it = fileItems.iterator();
			while (it.hasNext()) {
				FileItem fi = it.next();
				//判断是否为表单元素，是的话解析表单元素
				if (fi.isFormField()) {
					System.out.println("字段名：" + fi.getFieldName());
					System.out.println("字段值：" + fi.getString());
				} else {
					  String ym = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
					  String filePath = uploadpath+"/" + ym + fi.getName();
				//判断为上传文件、调用获取输入流
					InputStream in = fi.getInputStream();
					
					File fileout=new File(filePath);
					//准备输出流
					FileOutputStream out = new FileOutputStream(fileout);
					byte buffer[] = new byte[1024];
					int len = 0;
					//用缓冲区方式循环把输入流写入到输出流
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					//关闭输入输出流
					in.close();
					out.close();
					fi.delete();
					return fileout;
					
					
				}
			}
		} catch (FileUploadException e) {
			response.getWriter().write(e.toString());
		}
		return null;
	
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	this.doGet(request, response);
	}
	  public static List<HashMap<String, String>> importXlSX(File file) throws IOException{
		 Student stu = new Student();
		 FileInputStream is = new FileInputStream(file);
		  //用流创建表格
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		 XSSFSheet xsheet = workbook.getSheetAt(0);
		//装Student对象
			List<HashMap<String, String>> stulist=new ArrayList<HashMap<String, String>>();
		 if(xsheet!=null){
			XSSFRow frow = xsheet.getRow(0);
			//装数据库表字段
			List<String> list = new ArrayList<String>();
			for(int f=0;f<frow.getPhysicalNumberOfCells();f++){
				list.add(frow.getCell(f).toString());
			}
			 //sheet的所有行
			 for(int i=1;i<xsheet.getPhysicalNumberOfRows();i++){
				 //把student对象做成map
				 HashMap<String, String> map = new HashMap<String,String>();
				XSSFRow xrow = xsheet.getRow(i);
				for(int j=0;j<xrow.getPhysicalNumberOfCells();j++){
					String key=list.get(j);
					String value=xrow.getCell(j).toString();
					map.put(key, value);
				}
				stulist.add(map);
			 }
		 }
		 if(workbook!=null){
			 workbook.close();
		 }
		 if(is!=null){
			 is.close();
		 }
		 return stulist;
	  }
	
	  public static List<HashMap<String, String>> importXlS(File file) throws IOException{
			 Student stu = new Student();
			 FileInputStream is = new FileInputStream(file);
			  //用流创建表格
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			 HSSFSheet hsheet = workbook.getSheetAt(0);
			//装Student对象
				List<HashMap<String, String>> stulist=new ArrayList<HashMap<String, String>>();
			 if(hsheet!=null){
				HSSFRow frow = hsheet.getRow(1);
				//装数据库表字段
				List<String> list = new ArrayList<String>();
				for(int f=0;f<frow.getPhysicalNumberOfCells();f++){
					list.add(frow.getCell(f).toString());
				}
				 //sheet的所有行
				 for(int i=1;i<hsheet.getPhysicalNumberOfRows();i++){
					 //把student对象做成map
					 HashMap<String, String> map = new HashMap<String,String>();
					HSSFRow hrow = hsheet.getRow(i);
					for(int j=0;j<hrow.getPhysicalNumberOfCells();j++){
						String key=list.get(j);
						String value=hrow.getCell(j).toString();
						map.put(key, value);
					}
					stulist.add(map);
				 }
			 }
			 if(workbook!=null){
				 workbook.close();
			 }
			 if(is!=null){
				 is.close();
			 }
			 return stulist;
		  }
	  
	
}


