package com.va.voucher_request.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.va.voucher_request.model.Candidate;

public class ExcelHelper {
	
	public static boolean checkExcelFormat(MultipartFile file)
	{
		
		String contentType = file.getContentType();
		
		return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		
	}
	
	
	public static List<Candidate> convertExcelToListOfCandidate(InputStream is)
	{
		List<Candidate> candidatelist = new ArrayList<Candidate>();
		
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet =workbook.getSheet("candidates");
			int rowNumber=0;
			Iterator<Row> iterator = sheet.iterator();
			
			while(iterator.hasNext())
			{
				Row row = iterator.next();
				if(rowNumber==0)
				{
					rowNumber++;
					continue;
				}
				Iterator<Cell> cell = row.iterator();
				int cid =0;
				Candidate candidate = new Candidate();
				while(cell.hasNext())
				{
					Cell c = cell.next();
					
					switch(cid)
					{
					
					case 0 :
						candidate.setEmail(c.getStringCellValue().trim()); 
						break;
					case 1:
						candidate.setPractice(c.getStringCellValue().trim());
						break;
					case 2:
						candidate.setStatus(c.getStringCellValue().trim());
						break;
					default:
						break;
					
					}
					cid++;
				}
				System.out.println(candidate);
				candidatelist.add(candidate);
			}
			
	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return candidatelist;
	}

}