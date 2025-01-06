import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day2 {

	// returns count of total number of safe rows
	private int calculateSafeRows(List<List<Integer>> rowCollection) {
		int validRowCount = 0;
		for (List<Integer> row : rowCollection) {
			if (isSafeRow(row)) {
				validRowCount++;
			}
		}
		return validRowCount;
	}

	// returns count of total number of safe rows after 1 possible removal
	private int calculateSafeRowsAfterRemoval(List<List<Integer>> rowCollection) {
		int validRowCount = 0;
		for (List<Integer> row : rowCollection) {
			if (isSafeRow(row)) {
				validRowCount++;
			} else if (isSafeRowAfterRemoval(row)) {
				validRowCount++;
			}
		}
		return validRowCount;
	}

	// returns boolean determining if row is safe
	// row is safe if all numbers only increase or decrease within range of 3
	private boolean isSafeRow(List<Integer> row) {
		// check first pair in array to determine whether increase or decrease
		boolean isDecrease = false;
		int checkFirstDiff = row.get(0) - row.get(1); // 1
		isDecrease = checkFirstDiff > 0 ? true : false;

		// check if remaining array follow increase/decrease pattern
		for (int i = 0; i < row.size() - 1; i++) {
			int checkPairDiff = row.get(i) - row.get(i + 1);

			// if condition not met, return false
			if (isDecrease && (checkPairDiff > 3 || checkPairDiff < 1)) {
				return false;
			}
			if (!isDecrease && (checkPairDiff < -3 || checkPairDiff > -1)) {
				return false;
			}
		}
		// reached end of array, row is valid
		return true;
	}

	// returns boolean determining if row is safe
	// rows can still be valid after removal of one number
	private boolean isSafeRowAfterRemoval(List<Integer> row) {
		for (int i = 0; i < row.size(); i++) {
			// test every row after removal of one number
			List<Integer> rowAfterRemoval = new ArrayList<>(row);
			rowAfterRemoval.remove(i);

			// if condition met after removal, return true
			if (isSafeRow(rowAfterRemoval)) {
				return true;
			}
		}
		// reached end of all possible removals, row is invalid
		return false;
	}

	// parses each read line into array of numbers
	// returns all rows in 2d list
	private List<List<Integer>> processLineToLists(Scanner scanner) {
		List<List<Integer>> rowCollection = new ArrayList<>();
		while (scanner.hasNextLine()) {
			List<Integer> row = new ArrayList<>();
			String line = scanner.nextLine();
			// each number is split by white space, parse into integers
			String[] rowOfNums = line.split(" ");
			for (String num : rowOfNums) {
				row.add(Integer.parseInt(num));
			}
			rowCollection.add(row);
		}
		return rowCollection;
	}

	// reads file from input path
	// calls processLineToList function
	// returns all rows in 2d list
	private List<List<Integer>> readFile(String filePath) {
		List<List<Integer>> rowCollection = new ArrayList<>();
		try {
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);
			// build 2d list from processLineToList function
			rowCollection = processLineToLists(scanner);
			scanner.close();

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}
		return rowCollection;
	}

	// method to calculate solution for day 2 problem
	private void runDay2() {
		List<List<Integer>> rowCollection = readFile("day2.txt");
		if (rowCollection.isEmpty()) {
			return;
		}
		int totalSafeRows = calculateSafeRows(rowCollection);
		System.out.println("Total Safe Rows: " + totalSafeRows);
		int totalSafeRowsAfterRemoval = calculateSafeRowsAfterRemoval(rowCollection);
		System.out.println("Total Safe Rows After Removal: " + totalSafeRowsAfterRemoval);
	}
}
