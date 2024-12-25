import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day4 {

	// 2d list for board of characters
	private static List<List<Character>> board;

	// total count of possible "XMAS" formations
	private static int xmasCount;

	// total count of X patterns (see countXPatterninBoard() notes)
	private static int xPatternCount;

	/*
	 * traverses through board, counts number of possible X patterm formations.
	 * pattern must comprise of a two diagonal lines of characters 'm', 'a', 's'
	 * intersecting to form an X shape. characters may be in the order of 'm', 'a',
	 * 's', and 's', 'a', 'm'. intersecting lines do not have to share the same
	 * order
	 */
	public static void countXPatterninBoard() {
		for (int row = 0; row < board.size(); row++) {
			for (int col = 0; col < board.get(0).size(); col++) {
				// if center of pattern 'A' found, check if diagonals meet condition of having
				// 'm' , 's'
				if (board.get(row).get(col) == 'A') {
					// boundary check
					if (row - 1 >= 0 && row + 1 < board.size() && col - 1 >= 0 && col + 1 < board.get(0).size()) {
						// boolean to check if both diagonal conditions fulfilled
						boolean leftRightDiagonal = false;
						boolean rightLeftDiagonal = false;

						if (board.get(row - 1).get(col - 1) == 'M' && board.get(row + 1).get(col + 1) == 'S') {
							leftRightDiagonal = true;
						}
						if (board.get(row - 1).get(col - 1) == 'S' && board.get(row + 1).get(col + 1) == 'M') {
							leftRightDiagonal = true;
						}
						if (board.get(row - 1).get(col + 1) == 'S' && board.get(row + 1).get(col - 1) == 'M') {
							rightLeftDiagonal = true;
						}
						if (board.get(row - 1).get(col + 1) == 'M' && board.get(row + 1).get(col - 1) == 'S') {
							rightLeftDiagonal = true;
						}
						if (leftRightDiagonal && rightLeftDiagonal) {
							xPatternCount++;
						}
					}
				}
			}
		}
	}

	// traverses through board, counts number of possible "XMAS" formations
	public static void countXMASinBoard() {
		for (int row = 0; row < board.size(); row++) {
			for (int col = 0; col < board.get(0).size(); col++) {
				// if 'X' found, check all 8 possible directions for consecutive 'm', 'a', 's'
				// to form "XMAS"
				if (board.get(row).get(col) == 'X') {
					// boundary check prior to char check
					// up
					if (row - 3 >= 0) {
						if (board.get(row - 1).get(col) == 'M' && board.get(row - 2).get(col) == 'A'
								&& board.get(row - 3).get(col) == 'S') {
							xmasCount++;
						}
					}
					// down
					if (row + 3 < board.size()) {
						if (board.get(row + 1).get(col) == 'M' && board.get(row + 2).get(col) == 'A'
								&& board.get(row + 3).get(col) == 'S') {
							xmasCount++;
						}
					}
					// left
					if (col - 3 >= 0) {
						if (board.get(row).get(col - 1) == 'M' && board.get(row).get(col - 2) == 'A'
								&& board.get(row).get(col - 3) == 'S') {
							xmasCount++;
						}
					}
					// right
					if (col + 3 < board.get(0).size()) {
						if (board.get(row).get(col + 1) == 'M' && board.get(row).get(col + 2) == 'A'
								&& board.get(row).get(col + 3) == 'S') {
							xmasCount++;
						}
					}
					// diagonal up left
					if (row - 3 >= 0 && col - 3 >= 0) {
						if (board.get(row - 1).get(col - 1) == 'M' && board.get(row - 2).get(col - 2) == 'A'
								&& board.get(row - 3).get(col - 3) == 'S') {
							xmasCount++;
						}
					}
					// diagonal up right
					if (row - 3 >= 0 && col + 3 < board.get(0).size()) {
						if (board.get(row - 1).get(col + 1) == 'M' && board.get(row - 2).get(col + 2) == 'A'
								&& board.get(row - 3).get(col + 3) == 'S') {
							xmasCount++;
						}
					}
					// diagonal down left
					if (row + 3 < board.size() && col - 3 >= 0) {
						if (board.get(row + 1).get(col - 1) == 'M' && board.get(row + 2).get(col - 2) == 'A'
								&& board.get(row + 3).get(col - 3) == 'S') {
							xmasCount++;
						}
					}
					// diagonal down right
					if (row + 3 < board.size() && col + 3 < board.get(0).size()) {
						if (board.get(row + 1).get(col + 1) == 'M' && board.get(row + 2).get(col + 2) == 'A'
								&& board.get(row + 3).get(col + 3) == 'S') {
							xmasCount++;
						}
					}

				}
			}
		}
	}

	// adds each line as row of characters to boards
	public static void processLineToBoard(String line) {
		List<Character> row = new ArrayList<>();
		for (char ch : line.toCharArray()) {
			row.add(ch);
		}
		board.add(row);
	}

	// reads input file to process lines to board
	public static void processInputFile(String inputPath) {
		try {
			File file = new File(inputPath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				processLineToBoard(line);
			}
			scanner.close();

		} catch (FileNotFoundException error) {
			System.err.println("File not found: " + error.getMessage());
		}
	}

	// run solution for day 4
	public static void runDay4() {
		board = new ArrayList<>();
		xmasCount = 0;
		xPatternCount = 0;
		processInputFile("day4.txt");
		countXMASinBoard();
		countXPatterninBoard();
		System.out.println("Number of \"XMAS\" from input file: " + xmasCount);
		System.out.println("Number of \"X\" patterns from input file: " + xPatternCount);

	}
}
