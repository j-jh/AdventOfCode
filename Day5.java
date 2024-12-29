import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day5 {
	// stores key: number to a value: set
	// tracks ordering constraints: integer at key must come before numbers in set
	private final Map<Integer, Set<Integer>> numberToOrderSet;
	// 2d list of all rows from input file
	private final List<List<Integer>> listOfRows;
	// 2d list of valid rows
	private final List<List<Integer>> validRows;
	// 2d list of invalid rows (does not follow numberToOrderSet numberToOrderSet
	// ordering condition)
	private final List<List<Integer>> invalidRows;

	// constructor to initialize data structures
	// methods to populate data structures
	public Day5() {
		this.numberToOrderSet = new HashMap<>();
		this.listOfRows = new ArrayList<>();
		this.validRows = new ArrayList<>();
		this.invalidRows = new ArrayList<>();
		processInputFile("day5test.txt");
		sortRowsToLists();
	}

	// returns int sum of middle number from row data structure
	private int sumOfMiddleNumFromRows() {
		int sum = 0;
		for (List<Integer> row : validRows) {
			int middle = row.size() / 2;
			sum += row.get(middle);
		}
		return sum;
	}

	// adds rows from listOfRows to validRows and invalidRows based off
	// numberToOrderSet ordering constraints: integer at key must come before numbers in set
	private void sortRowsToLists() {
		for (List<Integer> row : listOfRows) {
			Set<Integer> currentRowNums = new HashSet<>();
			boolean isValidRow = true;
			for (int num : row) {
				if (!currentRowNums.isEmpty()) {
					// checks if numbers from current row exist in ordered set from map
					Set<Integer> intersection = new HashSet<>(currentRowNums);
					if (this.numberToOrderSet.containsKey(num)) {
						intersection.retainAll(this.numberToOrderSet.get(num));
						// if a number exists, intersection set will not be empty. set to invalid row.
						if (!intersection.isEmpty()) {
							isValidRow = false;
						}
					}
				}
				currentRowNums.add(num);
			}
			if (isValidRow) {
				this.validRows.add(row);
			} else {
				this.invalidRows.add(row);
			}
		}
	}

	// parses line to row of integers to add to listOfRows structure
	private void processSecondInputSectionToList(String line) {
		String[] charArray = line.split("\\,");
		List<Integer> row = new ArrayList<>();
		for (String ch : charArray) {
			row.add(Integer.parseInt(ch));
		}
		this.listOfRows.add(row);
	}

	// parses line to two integers to add to numberToOrderSet structure
	private void processFirstInputSectionToMap(String line) {
		String[] lineParts = line.split("\\|");
		// split by character literal
		int num = Integer.parseInt(lineParts[0]);
		int orderingNum = Integer.parseInt(lineParts[1]);
		if (!this.numberToOrderSet.containsKey(num)) {
			this.numberToOrderSet.put(num, new HashSet<>());
		}
		this.numberToOrderSet.get(num).add(orderingNum);

	}

	// processes input file
	// before blank line: passes each line to processSecondInputSectionToList()
	// after blank line: passes each line to processFirstInputSectionToMap()
	private void processInputFile(String inputPath) {
		boolean doneProcessingOrderings = false;
		// if line is blank, ordering process complete, proceed to process rows
		// matches test case format
		try {
			File file = new File(inputPath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isBlank()) {
					doneProcessingOrderings = true;
					continue;
				}
				if (doneProcessingOrderings == false) {
					processFirstInputSectionToMap(line);
				} else {
					processSecondInputSectionToList(line);
				}
			}
			scanner.close();
		} catch (FileNotFoundException error) {
			System.err.println("File not found: " + error.getMessage());
		}
	}

	// method to run day 5 solution
	public void runDay5Solution() {
		int sum = sumOfMiddleNumFromRows();
		System.out.println("Sum of middle number from valid rows: " + sum);
	}
}
