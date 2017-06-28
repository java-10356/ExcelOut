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
		//������Ӧͷ��ʽ������
		response.setContentType("text/html; charset=utf-8");
		//��һ�� �����ϴ����ļ��� д�뵽��� �洢Ŀ¼
		File file = (File) uploadFile(request,response);
		String lastm = file.getName().substring(file.getName().lastIndexOf(".")+1, file.getName().length());
		
		if(lastm.equals("XlS")||lastm.equals("xls")){
			//�ڶ�������ȡ������� ���list
			List<HashMap<String, String>> stulist = importXlS(file);
			//����������listѭ���������ݿ�
			StudentDaoImpl.insert(stulist);
		}else if(lastm.equals("XLSX")||lastm.equals("xlsx")){
			//��Ӧ��ͬ�汾Excle
			List<HashMap<String, String>> stulist = importXlSX(file);
			StudentDaoImpl.insert(stulist);
		}
		
	
	}

	
	private Object uploadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String webroot = this.getServletContext().getRealPath("/");
		File temppath = new File(webroot + "fileuploadtemp");//��ʱ�洢Ŀ¼
		File uploadpath = new File(webroot + "fileupload");//�洢Ŀ¼
		if (!temppath.exists()) {
			temppath.mkdirs();
		}
		if (!uploadpath.exists()) {
			uploadpath.mkdirs();
		}
        //����diskfileitemfactory�����࣬������ָ�Ĵ���������һ����ʱĿ¼
		DiskFileItemFactory factory = new DiskFileItemFactory(1024 * 1024,temppath);
		//����ServletFileUpoload�������������ʱĿ¼
		ServletFileUpload upload = new ServletFileUpload(factory);
		//�����ϴ��ļ����ֵ
		upload.setFileSizeMax(1024 * 1024 * 100);
        
		try {
			//����request������ϴ������ݽ����
			List<FileItem> fileItems = upload.parseRequest(request);
			//�������������
			Iterator<FileItem> it = fileItems.iterator();
			while (it.hasNext()) {
				FileItem fi = it.next();
				//�ж��Ƿ�Ϊ��Ԫ�أ��ǵĻ�������Ԫ��
				if (fi.isFormField()) {
					System.out.println("�ֶ�����" + fi.getFieldName());
					System.out.println("�ֶ�ֵ��" + fi.getString());
				} else {
					  String ym = new SimpleDateFormat("yyyy-MM-ddhhmmss").format(new Date());
					  String filePath = uploadpath+"/" + ym + fi.getName();
				//�ж�Ϊ�ϴ��ļ������û�ȡ������
					InputStream in = fi.getInputStream();
					
					File fileout=new File(filePath);
					//׼�������
					FileOutputStream out = new FileOutputStream(fileout);
					byte buffer[] = new byte[1024];
					int len = 0;
					//�û�������ʽѭ����������д�뵽�����
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					//�ر����������
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
		  //�����������
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		 XSSFSheet xsheet = workbook.getSheetAt(0);
		//װStudent����
			List<HashMap<String, String>> stulist=new ArrayList<HashMap<String, String>>();
		 if(xsheet!=null){
			XSSFRow frow = xsheet.getRow(0);
			//װ���ݿ���ֶ�
			List<String> list = new ArrayList<String>();
			for(int f=0;f<frow.getPhysicalNumberOfCells();f++){
				list.add(frow.getCell(f).toString());
			}
			 //sheet��������
			 for(int i=1;i<xsheet.getPhysicalNumberOfRows();i++){
				 //��student��������map
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
			  //�����������
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			 HSSFSheet hsheet = workbook.getSheetAt(0);
			//װStudent����
				List<HashMap<String, String>> stulist=new ArrayList<HashMap<String, String>>();
			 if(hsheet!=null){
				HSSFRow frow = hsheet.getRow(1);
				//װ���ݿ���ֶ�
				List<String> list = new ArrayList<String>();
				for(int f=0;f<frow.getPhysicalNumberOfCells();f++){
					list.add(frow.getCell(f).toString());
				}
				 //sheet��������
				 for(int i=1;i<hsheet.getPhysicalNumberOfRows();i++){
					 //��student��������map
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


