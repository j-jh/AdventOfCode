import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Objects;

public class Day6 {
	/*
	 * guard (indicated by a direction character) continuously moves around the
	 * board in the given direction until an obstacle is hit. the obstacle pushes
	 * the guard 90 degrees
	 */

	// ^ up
	// > right
	// v down
	// < left
	// # = obstacle, turn 90 degrees

	// constructor
	public Day6() {
		processInputFile("day6.txt");
		setStartingRowColPosition();
	}

	// helper class to store instances of row col position
	private class Coordinate {
		// maintain visibility for inherted visitedposition class
		protected int row;
		protected int col;

		// constructor to initialize
		private Coordinate(int row, int col) {
			this.row = row;
			this.col = col;
		}

		// override equals method. compares row, col values
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			// check if instance same class as obj
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			// cast to coordinate
			Coordinate other = (Coordinate) obj;
			// check values
			return this.row == other.row && this.col == other.col;
		}

		// override hashcode method after overriding equals method
		@Override
		public int hashCode() {
			return Objects.hash(row, col);
		}

		// tostring rep of coordinate object
		public String toString() {
			return "row: " + this.row + " col: " + this.col;
		}
	}

	// helper class to store instances of visitedposition
	// visited position inherits coordinate class,
	// includes direction to track direction at visited coordinate
	private class VisitedPosition extends Coordinate {
		// direction character of guard
		private char direction;

		// constructor to initialize
		private VisitedPosition(int row, int col, Character direction) {
			// takes base class constructor
			super(row, col);
			this.direction = direction;
		}

		// overriden equals method. compares row, col, direction values
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			// check if obj same type as instance
			if (obj.getClass() != this.getClass()) {
				return false;
			}
			// cast obj to type visitedposition
			VisitedPosition other = (VisitedPosition) obj;
			// returns row, col, direction check
			return super.equals(obj) && this.direction == other.direction;
		}

		// override hashcode method after overriding equals method
		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), direction);
		}

		// string representation of object
		@Override
		public String toString() {
			return super.toString() + " dir: " + this.direction;
		}
	}

	// 2d list for board
	private List<List<Character>> board = new ArrayList<>();
	// backup of inital board state for resetting
	private List<List<Character>> boardCopy = new ArrayList<>();
	// set to store instances of VisitedPosition object. tracks visited squares and
	// direction of visit
	private Set<VisitedPosition> seenPositions = new HashSet<>();

	// moves in given direction
	private int[][] directionMoves = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
	// character representation of moves
	private char[] directionChars = { '^', '>', 'v', '<' };

	// current row position
	private int rowPosition;
	// current column position
	private int colPosition;

	// populates board with rows of characters from line
	private void populateBoard(String line) {
		List<Character> row = new ArrayList<>();
		for (int i = 0; i < line.length(); i++) {
			row.add(line.charAt(i));
		}
		this.board.add(row);
	}

	// processes each line from input file to populate board
	private void processInputFile(String inputPath) {
		File file = new File(inputPath);
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				populateBoard(scanner.nextLine());
			}
			// saves copy of initial board state for future board state resetting
			saveBoardCopy();
		} catch (FileNotFoundException error) {
			System.out.println("Error: " + error);
		}
	}

	// helpful for debugging!
	private void printBoard() {
		for (List<Character> row : board) {
			for (Character ch : row) {
				System.out.print(ch + " ");
			}
			System.out.println();
		}
	}

	// saves copy of initial board state for future board state resetting
	private void saveBoardCopy() {
		this.boardCopy.clear();
		for (List<Character> row : this.board) {
			// deep copy!
			this.boardCopy.add(new ArrayList<>(row));
		}
	}

	// restores board from copied board state
	private void restoreBoardCopy() {
		this.board.clear();
		for (List<Character> row : this.boardCopy) {
			this.board.add(new ArrayList<>(row));
		}
	}

	// locate and assign inital row col position of guard character '^'
	private void setStartingRowColPosition() {
		for (int row = 0; row < this.board.size(); row++) {
			for (int col = 0; col < this.board.get(0).size(); col++) {
				if (this.board.get(row).get(col) == '^') {
					this.rowPosition = row;
					this.colPosition = col;
					return;
				}
			}
		}
	}

	// restores board to original state from processed input file
	// resets row col position
	// initializes variables to default values
	private void resetBoardToDefault() {
		directionCharacter = directionChars[0];
		directionCharsIndex = 0;
		count = 1;
		restoreBoardCopy();
		setStartingRowColPosition();
	}

	// check if move within board boundaries
	private boolean boundaryCheck(int rowMove, int colMove) {
		if (this.rowPosition + rowMove >= this.board.size() || this.rowPosition + rowMove < 0
				|| this.colPosition + colMove >= this.board.get(0).size() || this.colPosition + colMove < 0) {
			// out of bounds
			return false;
		}
		return true;
	}

	// count number of valid obstructions that give us a board loop
	private int countValidObstructionPlacements() {
		int loopAfterObstructionCount = 0;
		// set to stores unique coordinate instances. see coordinate class
		Set<Coordinate> uniqueCoordinates = new HashSet<>();
		// iterate through seenpositions set populated from countuniquevisits() function
		for (VisitedPosition obstacle : seenPositions) {
			Coordinate uniqueCoordinate = new Coordinate(obstacle.row, obstacle.col);

			// if coordinate unique
			if (checkIfUniqueCoordinate(uniqueCoordinates, uniqueCoordinate)) {
				// put obstacle at position
				board.get(obstacle.row).set(obstacle.col, '#');

			}
			// see if we get a loop
			if (containsLoopAfterObstruction()) {
				loopAfterObstructionCount++;
			}
			// reset board
			resetBoardToDefault();
		}
		return loopAfterObstructionCount;
	}

	// function to see if coordinate is unique within uniquecoordinates set
	private boolean checkIfUniqueCoordinate(Set<Coordinate> uniqueCoordinates, Coordinate uniqueCoordinate) {
		if (uniqueCoordinates.contains(uniqueCoordinate)) {
			return false;
		}
		// if unique, add
		uniqueCoordinates.add(uniqueCoordinate);
		return true;
	}

	// index at directionCharacter array
	int directionCharsIndex;
	// current character at directionChars array
	char directionCharacter;
	// count
	int count;

	// handles boundaries, movement, count, and board character update
	private int makeMove(int rowMove, int colMove) {
		// check if move is out of bounds
		if (!boundaryCheck(rowMove, colMove)) {
			// out of bounds
			return 1;
		}

		// calculate new position
		int newRow = rowPosition + rowMove;
		int newCol = colPosition + colMove;
		char targetCell = this.board.get(newRow).get(newCol);

		// within bounds to move
		if (targetCell == '#') {
			// rotate stance, continue
			directionCharsIndex++;
			directionCharacter = directionChars[directionCharsIndex % directionChars.length];
			return 2;
		}

		// move was successful
		if (targetCell == '.') {
			// unique visit
			count++;
		}

		// move directioncharacter
		this.board.get(newRow).set(newCol, directionCharacter);
		// mark visited square with 'x'
		this.board.get(rowPosition).set(colPosition, 'x');

		// update current position
		updatePosition(rowMove, colMove);
		return 3;
	}

	// update row and col position
	private void updatePosition(int rowMove, int colMove) {
		// update to move
		this.rowPosition += rowMove;
		this.colPosition += colMove;
	}

	// check if loop present in board
	// loop is defined: visit the same position with the same row, col, and
	// direction char twice
	private boolean containsLoopAfterObstruction() {
		// set to store unique visitedposition instances
		Set<VisitedPosition> currentPathPositions = new HashSet<>();
		while (true) {
			// cycles through indices, prevent out of bounds
			directionCharsIndex %= directionChars.length;

			// see directionmoves array for movement coordinates
			int rowMove = directionMoves[directionCharsIndex][0];
			int colMove = directionMoves[directionCharsIndex][1];

			// call makemove to handle boundaries, movement
			int makeMoveResult = makeMove(rowMove, colMove);
			if (makeMoveResult == 1) {
				// out of bounds
				break;
			} else if (makeMoveResult == 2) {
				// obstacle hit
				continue;
			}

			VisitedPosition checkVisitedPosition = new VisitedPosition(rowPosition, colPosition, directionCharacter);
			// add coordinate to currentpathpositions set
			if (currentPathPositions.contains(checkVisitedPosition)) {
				return true;
			}
			currentPathPositions.add(checkVisitedPosition);
		}
		return false;
	}

	// continuously move 1 step in given direction until boundarycheck() == false
	private int countUniqueVisits() {
		while (true) {
			// cycles through indices, prevent out of bounds
			directionCharsIndex %= directionChars.length;

			// see directionmoves array for movement coordinates
			int rowMove = directionMoves[directionCharsIndex][0];
			int colMove = directionMoves[directionCharsIndex][1];

			// call makemove to handle boundaries, movement
			int moveResult = makeMove(rowMove, colMove);

			if (moveResult == 1) {
				// out of bounds
				break;
			} else if (moveResult == 2) {
				// obstacle hit
				continue;
			}
			// add coordinate to seenpositions set
			seenPositions.add(new VisitedPosition(rowPosition, colPosition, directionCharacter));
		}
		// add final move to seenpositions
		seenPositions.add(new VisitedPosition(rowPosition, colPosition, directionCharacter));
		return count;
	}

	// runs solution
	public void runSolution() {
		System.out.println("number of unique squares visited: " + countUniqueVisits());
		resetBoardToDefault();
		System.out
				.println("number of unique obstacle placements to cause a loop: " + countValidObstructionPlacements());
	}

}
