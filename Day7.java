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
		for (String part : parts) {
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

		} catch (FileNotFoundException error) {
			System.out.println("Error: " + error);
		}
	}

	// calculate sums with and without concatnation operator
	private long[] calculateSumsFromEquations() {
		long[] results = new long[2];
		long sumWithAddAndMulti = 0;
		long sumWithConcatnation = 0;
		
		// for every sum in sums, every row in rows
		for (int i = 0; i < sums.size(); i++) {
			// variables for readability
			long targetValue = sums.get(i);
			long startingValue = rows.get(i).get(0);
			List<Long> row = rows.get(i);
			
			// if equation valid, add to sum
			if (isValidEquation(1, startingValue,targetValue, row)) {
				sumWithAddAndMulti += targetValue;
			} else if (isValidEquationWithConcatnation(1, startingValue,targetValue, row)) {
				// try concatnating and see if match
				sumWithConcatnation += targetValue;
			}
		}
		results[0] = sumWithAddAndMulti;
		results[1] = sumWithConcatnation + sumWithAddAndMulti;

		return results;
	}

	// check if equation is valid. able to obtain target sum through combination of
	// either multipliciation and/or addition
	private boolean isValidEquation(int index, long rowSum, long target, List<Long> row) {
		// return when last index
		if (index == row.size()) {
			return rowSum == target;
		}
		// check addition
		boolean addition = isValidEquation(index + 1, rowSum + row.get(index), target, row);
		// check multiplication
		boolean multiplication = isValidEquation(index + 1, rowSum * row.get(index), target, row);
		// return if either options valid
		return multiplication || addition;
	}

	// check if equation is valid with additional concatnation operator
	// concatnates values left (sum) to right (current val)
	// also refer to isValidEquation()
	private boolean isValidEquationWithConcatnation(int index, long rowSum, long target, List<Long> row) {
		if (index == row.size()) {
			// return when last index
			return rowSum == target;
		}
		// check addition
		boolean addition = isValidEquationWithConcatnation(index + 1, rowSum + row.get(index), target, row);
		// check multiplication
		boolean multiplication = isValidEquationWithConcatnation(index + 1, rowSum * row.get(index), target, row);

		// concatnation operator, concatnates values left (sum) to right (current val)
		long concatnatedValue = Long.parseLong(String.valueOf(rowSum) + String.valueOf(row.get(index)));
		boolean concatnation = isValidEquationWithConcatnation(index + 1, concatnatedValue, target, row);
		// return if either options valid
		return multiplication || addition || concatnation;
	}

	// run day 7 solution
	public void runSolution() {
		System.out.println("Sum of values from valid equations: " + calculateSumsFromEquations()[0]);
		System.out.println(
				"Sum of values from valid equations with concatnation: " + calculateSumsFromEquations()[1]);

	}
}
