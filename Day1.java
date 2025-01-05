import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day1 {
	// calculates values from column one * frequency from column 2
	private int calculateSimilarityScore(List<Integer> columnOneList, List<Integer> columnTwoList) {
		int similarityScore = 0;
		Map<Integer, Integer> numToFreq = new HashMap<>();
		// count freq of col 2 values
		for (int num : columnTwoList) {
			numToFreq.put(num, numToFreq.getOrDefault(num, 0) + 1);
		}
		// multiply col 1 values by col 2 freq
		for (int num : columnOneList) {
			if (numToFreq.containsKey(num)) {
				similarityScore += num * numToFreq.get(num);
			}
		}
		return similarityScore;
	}

	// calculates total sum of differences between two lists' number pairs
	private int calculateSumFromLists(List<Integer> columnOneList, List<Integer> columnTwoList) {
		int totalSum = 0;
		List<Integer> list1 = columnOneList;
		List<Integer> list2 = columnTwoList;

		Collections.sort(list1);
		Collections.sort(list2);
		for (int i = 0; i < columnOneList.size(); i++) {
			int pairDiff = Math.abs(list1.get(i) - list2.get(i));
			totalSum += pairDiff;
		}

		return totalSum;
	}
	// processes file/split by white space and add to two array lists
	private List<List<Integer>> processFileToLists(Scanner scanner) {
		List<Integer> columnOneList = new ArrayList<>();
		List<Integer> columnTwoList = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// file is consistent triple white space split
			String[] lineParts = line.split("   ");
			int colOneNum = Integer.parseInt(lineParts[0]);
			int colTwoNum = Integer.parseInt(lineParts[1]);
			columnOneList.add(colOneNum);
			columnTwoList.add(colTwoNum);
		}
		List<List<Integer>> listCollection = new ArrayList<>();
		listCollection.add(columnOneList);
		listCollection.add(columnTwoList);
		return listCollection;
	}
	private List<List<Integer>> readFile(String filePath){
		List<List<Integer>> listCollection = new ArrayList<>();
		try {
			// file object for input path
			File file = new File(filePath);
			// scanner for reading lines
			Scanner scanner = new Scanner(file);
			listCollection = processFileToLists(scanner);
			scanner.close();

		} catch (FileNotFoundException e) {
			// throws file not found err
			System.err.println("File not found: " + e.getMessage());
		}
		return listCollection;
	}
	
	private void runDay1() {
		System.out.println("Hello LeetCode!");
		List<Integer> columnOneList;
		List<Integer> columnTwoList;
		List<List<Integer>> listCollection = readFile("day1.txt");
		if (listCollection.isEmpty()) {
			return;
		}
		columnOneList = listCollection.get(0);
		columnTwoList = listCollection.get(1);
		int totalSum = calculateSumFromLists(columnOneList, columnTwoList);
		int similarityScore = calculateSimilarityScore(columnOneList, columnTwoList);
		System.out.println("Total sum of pairs: " + totalSum);
		System.out.println("Similarity Score: " + similarityScore);
	}
}
