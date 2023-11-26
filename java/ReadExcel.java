import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList; // The ArrayList class is a resizable array
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	public String convertToCSV(String[] data) {
		return Stream.of(data)
			.map(this::escapeSpecialCharacters)
			.collect(Collectors.joining(","));
	}

	public String escapeSpecialCharacters(String data) {
		if (data == null) {
			throw new IllegalArgumentException("Input data cannot be null");
		}
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

	public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
		File csvOutputFile = new File("/opt/irisapp/CSVfile.csv");
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream()
				.map(this::convertToCSV)
				.forEach(pw::println);
		}
		assertTrue(csvOutputFile.exists());
	}

    public static void main(String[] args) throws IOException {
	    List<String[]> dataLines = new ArrayList<>();
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
			ArrayList<String> myRow = new ArrayList<String>(); // Create an ArrayList object
			for (int col = 0; col < sheet.getColumns(); col++) {
				
                    Cell cell = sheet.getCell(col, row);
                    CellType type = cell.getType();
                    if (type == CellType.LABEL) {
                        System.out.println("I got a label " + cell.getContents());
			    myRow.add(cell.getContents());
                    }

                    if (type == CellType.NUMBER) {
                        System.out.println("I got a number " + cell.getContents());
			    myRow.add(cell.getContents());
                    }

                }
			dataLines.add(myRow);
			//ArrayList<String> csvRow = convertToCSV(myRow);
			//myWriter.write(csvRow + System.lineSeparator());
			givenDataArray_whenConvertToCSV_thenOutputCreated();
            }
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
        } catch (BiffException e) {
		System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
