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
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over rows and columns
            for (int row = 0; row < sheet.getRows(); row++) {
                //ArrayList<String> myRow = new ArrayList<String>(); // Create an ArrayList object
                for (int col = 0; col < sheet.getColumns(); col++) {
                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();
                    if (type == CellType.LABEL) {
                        System.out.println("I got a label " + cell.getContents());
                        String myLabel = cell.getContents();
			// ObjectScript equivalent: set ^excel("1",row,col) = ...
                        //iris.set(myLabel,"^excel","1",row,col);

                    }

                    if (type == CellType.NUMBER) {
                        System.out.println("I got a number " + cell.getContents());
                        int myNumber = cell.getContents();
       			// ObjectScript equivalent: set ^excel("1",row,col) = ...
                        //iris.set(myNumber,"^excel","1",row,col);

                    }
                    // ObjectScript equivalent: set ^excel("1",row,col) = ...
                    iris.set(cell.getContents(),"^excel","1",row,col);

                }
			
            }
		
        } catch (BiffException e) {
		System.out.println("An error occurred.");
            e.printStackTrace();
        }


            System.out.println("[1. Setting and getting a global]");

            // setting and getting a global
            // ObjectScript equivalent: set ^testglobal("1") = 8888
            iris.set(8888,"^testglobal","1");
            // ObjectScript equivalent: set globalValue = $get(^testglobal("1"))
            Integer globalValue = iris.getInteger("^testglobal","1");

            System.out.println("The value of ^testglobal(1) is " + globalValue);
            System.out.println();



            System.out.println("[2. Iterating over a global]");

            // modify global to iterate over
            // ObjectScript equivalent: set ^testglobal("1") = 8888
            // ObjectScript equivalent: set ^testglobal("2") = 9999
            iris.set(8888,"^testglobal","1");
            iris.set(9999,"^testglobal","2");

            // iterate over all nodes forwards
            IRISIterator subscriptIter = iris.getIRISIterator("^testglobal");
            System.out.println("walk forwards");
            while (subscriptIter.hasNext()) {
                String subscript = subscriptIter.next();
                System.out.println("subscript="+subscript+", value="+subscriptIter.getValue());
            }

            System.out.println();

            System.out.println("[3. Calling a class method]");

            // calling a class method
            // ObjectScript equivalent: set returnValue = ##class(%Library.Utility).Date(5)
            String returnValue = iris.classMethodString("%Library.Utility","Date",5);
            System.out.println(returnValue);

            System.out.println();

            // close connection and IRIS object
            iris.close();
            conn.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
