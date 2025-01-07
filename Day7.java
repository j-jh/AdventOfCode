import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day7 {
	// list to store sums
	private List<Long> sums;
	// list to store row of numbers
	private List<List<Long>> rows;
	
	// constructor to initalize structures, process file
	public Day7() {
		sums = new ArrayList<>();
		rows = new ArrayList<>();
		processInputFile("day7.txt");
	}
	// process each line to a row. add row to list of rows
	private void processRows(String line) {
		String[] parts = line.split(" ");
		List<Long> row = new ArrayList<>();
		for (String part: parts) {
			row.add(Long.parseLong(part));
		}
		rows.add(row);
	}
	
	// process input file from input path
	private void processInputFile(String inputPath) {
		File file = new File(inputPath);
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(": ");
				sums.add(Long.parseLong(parts[0]));
				processRows(parts[1]);
			}
			
		} catch (FileNotFoundException error){
			System.out.println("Error: "+ error);
		}
	}
	
	// calculate sum of equations that are valid
	private long calculateSumsFromValidEquations() {
		long sum = 0;
		for (int i = 0; i < sums.size(); i++) {
			if (isValidEquation(1,rows.get(i).get(0), sums.get(i), rows.get(i))) {
				sum += sums.get(i);
			} else {
				// try concatnating and see if match 
			}
		}
		
		return sum;
	}
	
	// check if equation is valid. able to obtain target sum through combination of either multipliciation and/or addition
	private boolean isValidEquation(int index, long rowSum, long target, List<Long> row) {
		if (index == row.size()) {
			return rowSum == target;
		}
		boolean addition = isValidEquation(index+1, rowSum + row.get(index), target, row);
		boolean multiplication = isValidEquation(index+1, rowSum * row.get(index), target, row);
		return multiplication || addition;
	}
	
	// run day 7 solution
	public void runSolution() {
		System.out.println("Sum of values from valid equations: " + calculateSumsFromValidEquations());
	}
}
