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
	private final Map<Integer, Set<Integer>> numberToOrderSet;
	private final List<List<Integer>> listOfRows;
	private final List<List<Integer>> validRows;
	private final List<List<Integer>> invalidRows;

	public Day5() {
		this.numberToOrderSet = new HashMap<>();
		this.listOfRows = new ArrayList<>();
		this.validRows = new ArrayList<>();
		this.invalidRows = new ArrayList<>();
		processInputFile("day5test.txt");
		sortRowsToLists();
	}

	private int sumOfMiddleNumFromRows() {
		int sum = 0;
		for (List<Integer> row : validRows) {
			int middle = row.size() / 2;
			sum += row.get(middle);
		}
		return sum;
	}

	private void sortRowsToLists() {
		for (List<Integer> row : listOfRows) {
			Set<Integer> currentRowNums = new HashSet<>();
			boolean isValidRow = true;
			for (int num : row) {
				if (!currentRowNums.isEmpty()) {
					Set<Integer> intersection = new HashSet<>(currentRowNums);
					if (this.numberToOrderSet.containsKey(num)) {
						intersection.retainAll(this.numberToOrderSet.get(num));
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

	private void processSecondInputSectionToList(String line) {
		String[] charArray = line.split("\\,");
		List<Integer> row = new ArrayList<>();
		for (String ch : charArray) {
			row.add(Integer.parseInt(ch));
		}
		this.listOfRows.add(row);
	}

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

	public void runDay5Solution() {
		int sum = sumOfMiddleNumFromRows();
		System.out.println("Sum of middle number from valid rows: "+ sum);
	}
}
