import java.io.File;
import java.sql.DriverManager;
import java.util.Scanner;
import com.intersystems.jdbc.IRISConnection;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISIterator;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class IRISNative {

    protected static int superserverPort = 1972;
    protected static String namespace = "USER";
    protected static String username = "_SYSTEM";
    protected static String password = "SYS";

    public static String cmd(String what, String hint) {
        Scanner sc = new Scanner(System.in);
        System.out.print(">>> "+what+" ["+hint+"]: ");
        String ans = sc.nextLine();
        if (ans.isEmpty()) {
            ans = hint;
        }
        return ans;
    }
    public static void main(String[] args) {
        try {
            // open connection to InterSystems IRIS instance using connection string
            IRISConnection conn = (IRISConnection) DriverManager.getConnection
                    ("jdbc:IRIS://localhost:"+superserverPort+"/"+namespace,username,password);
            // create Native API object
            IRIS iris = IRIS.createIRIS(conn);
            String inputFile = cmd("Input File","/opt/irisapp/excel/money.xls");
	    File inputWorkbook = new File(inputFile);
            Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
	for (int sheetNum = 0; sheetNum < w.getNumberOfSheets(); sheetNum++) {
            // Get the [sheetNum] sheet
            Sheet sheet = w.getSheet(sheetNum);
            // Loop over rows and columns
            for (int row = 0; row < sheet.getRows(); row++) {
                for (int col = 0; col < sheet.getColumns(); col++) {
                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();
                    // if (type == CellType.LABEL) {
                        // System.out.println("I got a label " + cell.getContents());
                        String myLabel = cell.getContents();
			if (myLabel == "") {
				continue;  // Skip empty cells
			}
			// ObjectScript equivalent: set ^excel("1",row,col) = ...
                        iris.set(myLabel,"^excel",sheetNum,row,col);

                    // }

                    // if (type == CellType.NUMBER) {
                        // System.out.println("I got a number " + cell.getContents());
                        // String myNumber = cell.getContents();
       			// ObjectScript equivalent: set ^excel("1",row,col) = ...
                        // iris.set(myNumber,"^excel",sheetNum,row,col);

                    // }
                    // ObjectScript equivalent: set ^excel("1",row,col) = ...
                    //iris.set(cell.getContents(),"^excel","1",row,col);

                }
			
            }
	}
        } catch (BiffException e) {
		System.out.println("An error occurred.");
            e.printStackTrace();
        }



            // close connection and IRIS object
            iris.close();
            conn.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
