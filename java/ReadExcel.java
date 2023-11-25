import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {

    public static String cmd(String what, String default) {
		Scanner sc = new Scanner(System.in);
		System.out.print(">>> "+what+" ["+default+"]: ");
		String ans = sc.nextLine();
        if (ans.isEmpty()) {
            ans = default;
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        String inputFile = cmd("Input File","/opt/irisapp/excel/money.xls");
        File inputWorkbook = new File(inputFile);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over columns and lines
            for (int col = 0; col < sheet.getColumns(); col++) {
                for (int row = 0; row < sheet.getRows(); row++) {
                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();
                    if (type == CellType.LABEL) {
                        System.out.println("I got a label "
                                + cell.getContents());
                    }

                    if (type == CellType.NUMBER) {
                        System.out.println("I got a number "
                                + cell.getContents());
                    }

                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }

}
