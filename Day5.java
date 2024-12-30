import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day5 {
	// stores key: number to a value: set
	// tracks ordering constraints: integer at key must come before numbers in set
	private Map<Integer, Set<Integer>> numberToOrderSet;
	// 2d list of all rows from input file
	private List<List<Integer>> listOfRows;
	// sum of middle number from all valid rows
	private int sumOfValidRowMiddleNums;
	// sum of middle number from all invalid rows
	private int sumOfInvalidRowMiddleNums;

	// constructor to initialize variables
	// methods to populate data structures
	public Day5() {
		this.numberToOrderSet = new HashMap<>();
		this.listOfRows = new ArrayList<>();
		sumOfValidRowMiddleNums = 0;
		sumOfInvalidRowMiddleNums = 0;
		processInputFile("day5.txt");
		sortRowsToLists();
	}

	// takes input row, sorts each num in row based off ordering constraints from
	// numberToOrderSet map
	// integers at key must come before numbers in value set
	private List<Integer> customRowSorter(List<Integer> row) {
		List<Integer> sortedRow = row;
		sortedRow.sort((a, b) -> {
			if (a.equals(b)) {
				// if equal, no order
				return 0;
			}
			if (this.numberToOrderSet.containsKey(a) && this.numberToOrderSet.get(a).contains(b)) {
				// when a comes before b
				return -1;
			}
			if (this.numberToOrderSet.containsKey(b) && this.numberToOrderSet.get(b).contains(a)) {
				// when b comes before a
				return 1;
			}
			// if no constraints in map, no order
			return 0;
		});
		return sortedRow;
	}

	// adds rows from listOfRows to validRows and invalidRows based off
	// numberToOrderSet ordering constraints: integer at key must come before
	// numbers in set
	private void sortRowsToLists() {
		for (List<Integer> row : listOfRows) {
			Set<Integer> seenRowNums = new HashSet<>();
			boolean isValidRow = true;
			for (int num : row) {
				if (!seenRowNums.isEmpty())
					isValidRow = checkIfOverlapWithSeenRowNums(num, seenRowNums);
				if (!isValidRow) {
					List<Integer> sortedInvalidRow = customRowSorter(row);
					this.sumOfInvalidRowMiddleNums += sortedInvalidRow.get(sortedInvalidRow.size() / 2);
					break;
				}
				seenRowNums.add(num);
			}
			if (isValidRow) {
				this.sumOfValidRowMiddleNums += row.get(row.size() / 2);
			}
		}
	}

	// checks if there is overlap from currentRowNums to value ordering set in map
	// key
	// nice little comparison method
	private boolean checkIfOverlapWithSeenRowNums(int key, Set<Integer> seenRowNums) {
		if (this.numberToOrderSet.containsKey(key)) {
			for (int num : seenRowNums) {
				if (this.numberToOrderSet.get(key).contains(num)) {
					return false;
				}
			}
		}
		return true;
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
		File file = new File(inputPath);
		try (Scanner scanner = new Scanner(file)) { // try with resources automatically closes scanner
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isBlank()) {
					doneProcessingOrderings = true;
					continue;
				}
				if (!doneProcessingOrderings) {
					processFirstInputSectionToMap(line);
				} else {
					processSecondInputSectionToList(line);
				}
			}
		} catch (FileNotFoundException error) {
			System.err.println("File not found: " + error.getMessage());
		}
	}

	// method to run day 5 solution
	public void runDay5Solution() {
		System.out.println("Sum of middle number from valid rows: " + this.sumOfValidRowMiddleNums);
		System.out.println("Sum of middle number from sorted invalid rows: " + this.sumOfInvalidRowMiddleNums);
	}
}
