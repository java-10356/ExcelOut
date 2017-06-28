package Servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.offcn.DaoImpl.StudentDaoImpl;
import com.offcn.Utils.Demo1;

public class OutServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("ѧ����");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = null;
		int i = 0;
		// �õ������������ֵValue
		HashMap map1 = (HashMap) StudentDaoImpl.getAll().get(0);
		for (Object entry : map1.entrySet()) {
			Entry mapen = (Map.Entry) entry;
			// i��ʾ�ڼ���
			cell = row.createCell((short) i);
			cell.setCellValue(mapen.getKey().toString());
			i++;
		}
	
		// ÿ������ ռ һ��
		for (int j = 0; j < StudentDaoImpl.getAll().size(); j++) {
			
			XSSFRow rowid = sheet.createRow(j+1);
			HashMap smap = (HashMap) StudentDaoImpl.getAll().get(j);
			int k=0;
			for (Object entry : smap.entrySet()) {
				Entry mapen = (Map.Entry) entry;
				System.out.println(mapen);
				cell = rowid.createCell((short) k);
				cell.setCellValue(mapen.getValue().toString());
				k++;
			}
		}
		response.setContentType("octets/stream");  
//	      response.addHeader("Content-Disposition", "attachment;filename=test.xls");  
	        String excelName = "tongxunlu";  
	        //ת���ֹ����  
	        response.addHeader("Content-Disposition", "attachment;filename="+excelName+".xlsx");
		 OutputStream out = response.getOutputStream();  
		 //�� ���úõ�workbook��  д��response��
		workbook.write(out);
		out.close();
		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
