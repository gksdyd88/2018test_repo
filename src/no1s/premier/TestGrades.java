package no1s.premier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class TestGrades {
	public static void main(String... args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		String excelDir = "res/data.xlsx";
		// エクセル読み込み
		Workbook wb = WorkbookFactory.create(new File(excelDir));
		Sheet sheet = wb.getSheet("students");
		
		// ヘッダー
		System.out.println("名前" + "\t\t" + "国語" + "\t" + "数学" + "\t" + "英語" + "\t" + "社会" + "\t" + "理科" + "\t" + "合計点");

		// 全行取り出し
		Iterator<Row> rows = sheet.rowIterator();
		int rowCount = 0;
		while(rows.hasNext()) {
			Row row = rows.next();
			if(rowCount >= 2) {				
				//全セル取り出し
				Iterator<Cell> cells = row.cellIterator();
				int cellCount = 0;
				int sum = 0;
				ArrayList<String> valueList = new ArrayList<String>();
				
				while(cells.hasNext()) {
					Cell cell = cells.next();
					if(cellCount <= 1) {
						String strValue = cell.getStringCellValue();
						valueList.add(strValue);
					} else {
						int numValue = (int) cell.getNumericCellValue();
						sum += numValue;
						valueList.add(String.valueOf(numValue));
					}
					cellCount++;
				}
				
				// 行ごとに形式合わせて表示
				valueList.add(String.valueOf(sum));
				for(int i = 0; i < valueList.size(); i++) {
					String value = valueList.get(i);
					if(i < 1) {
						System.out.print(value + " ");
					} else if (i == 1) {
						System.out.print(value + "\t");
						if(value.equals("さくら")) {
							System.out.print("\t");
						}
						if(value.length() < 3) {
							System.out.print("\t");
						}
					} else {
						System.out.print(value + "\t");
					}
				}
				System.out.println("");
			}
			rowCount++;
		}
	}
}
