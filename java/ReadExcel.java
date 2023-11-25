import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {

    public static String cmd(String what, String hint) {
		Scanner sc = new Scanner(System.in);
		System.out.print(">>> "+what+" ["+hint+"]: ");
		String ans = sc.nextLine();
        if (ans.isEmpty()) {
            ans = hint;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
	    String inputFile = cmd("Input File","/opt/irisapp/excel/money.xls");
	    File inputWorkbook = new File(inputFile);
	    FileWriter myWriter = new FileWriter("/opt/irisapp/filename.txt");
	    Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over rows and columns
		for (int row = 0; row < sheet.getRows(); row++) {
			for (int col = 0; col < sheet.getColumns(); col++) {
				
                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();
                    if (type == CellType.LABEL) {
                        System.out.println("I got a label " + cell.getContents());
                    }

                    if (type == CellType.NUMBER) {
                        System.out.println("I got a number " + cell.getContents());
                    }

                }
            }
		myWriter.write("Files in Java might be tricky, but it is fun enough!");
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
        } catch (BiffException e) {
		System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
