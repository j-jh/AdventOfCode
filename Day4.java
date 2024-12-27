import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day4 {

	// 2d list for board of characters
	private final List<List<Character>> board;
	
	// map to store key: string direction value: int[] row col changes for direction
	private final Map<String, int[]> directions;

	// constructor to initialize board, directions
	// populates directions using setDirections()
	public Day4() {
		this.board = new ArrayList<>();
		this.directions = new HashMap<>();
		setDirections();
	}

	// populates directions map with 8 direction key-value pairs
	// key: string direction value: int[] row col changes
	private void setDirections() {
		directions.put("up", new int[] { -1, 0 });
		directions.put("down", new int[] { 1, 0 });
		directions.put("left", new int[] { 0, -1 });
		directions.put("right", new int[] { 0, 1 });
		directions.put("up left", new int[] { -1, -1 });
		directions.put("up right", new int[] { -1, 1 });
		directions.put("down left", new int[] { 1, -1 });
		directions.put("down right", new int[] { 1, 1 });
	}

	/*
	 * traverses through board, counts number of possible X patterm formations.
	 * pattern must comprise of a two diagonal lines of characters 'm', 'a', 's'
	 * intersecting to form an X shape. characters may be in the order of 'm', 'a',
	 * 's', and 's', 'a', 'm'. intersecting lines do not have to share the same
	 * order
	 */
	private int countXPatterninBoard() {
		int xPatternCount = 0;
		for (int row = 0; row < board.size(); row++) {
			for (int col = 0; col < board.get(0).size(); col++) {
				// if center of pattern 'A' found, check if diagonals meet condition of having
				// 'm' , 's'
				if (board.get(row).get(col) == 'A' && isValidDiagonalPattern(row, col)) {
					// if char starts with 'A' and fulfills diagonal pattern conditions, is valid
					// pattern
					xPatternCount++;

				}
			}
		}
		return xPatternCount;
	}

	// checks whether current row, col position fulfills diagonal pattern conditions
	// see countXPatterninBoard() notes
	private boolean isValidDiagonalPattern(int row, int col) {
		// bound check to see if we are within board boundaries
		if (row - 1 >= 0 && row + 1 < board.size() && col - 1 >= 0 && col + 1 < board.get(0).size()) {
			// boolean to check if both diagonal conditions fulfilled
			boolean leftRightDiagonal = false;
			boolean rightLeftDiagonal = false;

			// diagonal conditions
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
			return leftRightDiagonal && rightLeftDiagonal;
		}
		// return false if out of bounds
		return false;
	}

	// checks current direction directionKey at position row, col
	// returns true or false if possible to form "XMAS" at current position
	private boolean checkDirectionsForXMAS(int row, int col, String directionKey) {
		// get direction from map
		int[] direction = this.directions.get(directionKey);
		int rowDirection = direction[0];
		int colDirection = direction[1];
		// boundary check if +3 to current position to form "XMAS" goes out of bounds
		if (row + rowDirection * 3 < 0 || row + rowDirection * 3 >= board.size() || col + colDirection * 3 < 0
				|| col + colDirection * 3 >= board.get(0).size()) {
			return false;
		}
		// check for presence of 'M', 'A', 'S'
		boolean checkM = (board.get(row + rowDirection).get(col + colDirection) == 'M');
		boolean checkA = (board.get(row + rowDirection * 2).get(col + colDirection * 2) == 'A');
		boolean checkS = (board.get(row + rowDirection * 3).get(col + colDirection * 3) == 'S');
		// return true if present
		return checkM && checkA && checkS;
	}

	// traverses through board, counts number of possible "XMAS" formations
	private int countXMASinBoard() {
		int xmasCount = 0;
		for (int row = 0; row < board.size(); row++) {
			for (int col = 0; col < board.get(0).size(); col++) {
				// if 'X' found, check all 8 possible directions for consecutive 'm', 'a', 's'
				// to form "XMAS"
				if (board.get(row).get(col) == 'X') {
					for (String direction : this.directions.keySet()) {
						// if fulfills funciton to check all 8 directions, increment count
						if (checkDirectionsForXMAS(row, col, direction)) {
							xmasCount++;
						}
					}

				}
			}
		}
		return xmasCount;
	}

	// adds each line as row of characters to boards
	private void processLineToBoard(String line) {
		List<Character> row = new ArrayList<>();
		for (char ch : line.toCharArray()) {
			row.add(ch);
		}
		board.add(row);
	}

	// reads input file to process lines to board
	private void processInputFile(String inputPath) {
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
	public void runDay4() {
		processInputFile("day4.txt");
		int xmasCount = countXMASinBoard();
		int xPatternCount = countXPatterninBoard();
		System.out.println("Number of \"XMAS\" from input file: " + xmasCount);
		System.out.println("Number of \"X\" patterns from input file: " + xPatternCount);

	}
}
