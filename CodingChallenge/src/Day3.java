import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 {
	// xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
	// only mul(2,4) mul(5,5) mul(11,8) mul(8,5)

	// if mul( __ only num , only num__ )

	// returns total sum of all valid patterns from all parsed lines
	public static int calculateSumOfAllPatternLines(List<String> lines) {
		int totalSum = 0;
		for (String line : lines) {
			totalSum += calculateSumOfPatternProducts(line);
		}
		return totalSum;
	}

	// returns the total sum of all valid pattern products from the input line
	// must match format: "mul(" integer ',' integer ')'
	public static int calculateSumOfPatternProducts(String line) {
		int totalPatternSum = 0;
		for (int i = 0; i < line.length(); i++) {
			// grab substring which starts with "mul("
			if (line.startsWith("mul(", i)) {
				// complete substring ending at ')'
				int closingParenthesis = line.indexOf(')', i);
				if (closingParenthesis != -1) {
					// pass contents inside mul( ___ ) to check if valid
					// i+4 accounts for "mul("
					String substringToCheck = line.substring(i + 4, closingParenthesis);
					if (isValidSubstringPattern(substringToCheck)) {
						String[] substringSections = substringToCheck.split(",");
						int num1 = Integer.parseInt(substringSections[0]);
						int num2 = Integer.parseInt(substringSections[1]);
						totalPatternSum += num1 * num2;
					}
				}
			}
		}
		return totalPatternSum;
	}
	
	// returns total sum of all valid patterns from all parsed lines
	public static int calculateSumOfAllPatternLinesWithCondition(List<String> lines) {
		int totalSum = 0;
		boolean previousLineContinue = true;
		for (String line : lines) {
			Object[] output = calculateSumOfPatternProductsWithCondition(line, previousLineContinue);
			totalSum += (Integer)output[0];
			previousLineContinue = (Boolean)output[1];
			
		}
		return totalSum;
	}
	
//	// potential issue? retains do() or don't() instruction for the next line ????
//	public static boolean continueProcess(String line) {
//		boolean continueProcess = false;
//		for (int i = 0; i < line.length(); i++) {
//			if (line.startsWith("don't()", i)) {
//				continueProcess = false;
//				i +=6;
//				continue;
//			}
//			if (line.startsWith("do()", i)) {
//				continueProcess = true;
//				i+=3;
//				continue;
//			}
//		}
//		return continueProcess;
//	}
	
	// returns total sum of all valid pattern products from input line, factors do() don't() functionality
	// "do()" continue to process line
	// "don't()" negates "mul(" integer ',' integer ')'
	public static Object[] calculateSumOfPatternProductsWithCondition(String line, boolean previousLineContinue) {
		int totalPatternSum = 0;
		boolean continueProcess = previousLineContinue;
		for (int i = 0; i < line.length(); i++) {
			if (line.startsWith("don't()", i)) {
				continueProcess = false;
				i +=6;
				continue;
			}
			if (line.startsWith("do()", i)) {
				continueProcess = true;
				i+=3;
				continue;
			}
			// grab substring which starts with "mul("
			if (line.startsWith("mul(", i) && continueProcess) {
				// complete substring ending at ')'
				int closingParenthesis = line.indexOf(')', i);
				if (closingParenthesis != -1) {
					// pass contents inside mul( ___ ) to check if valid
					// i+4 accounts for "mul("
					String substringToCheck = line.substring(i + 4, closingParenthesis);
					if (isValidSubstringPattern(substringToCheck)) {
						String[] substringSections = substringToCheck.split(",");
						int num1 = Integer.parseInt(substringSections[0]);
						int num2 = Integer.parseInt(substringSections[1]);
						totalPatternSum += num1 * num2;
					}
				}
				
			}
		}
		// need to fix
//		return new Object[] {totalPatternSum, continueProcess};
		return null;
	}

	// check if substring inside mul(__) follows pattern of integer ',' integer
	public static boolean isValidSubstringPattern(String substring) {
		// check if can be split into two parts
		String[] substringSections = substring.split(",");
		if (substringSections.length != 2) {
			return false;
		}
		// check if parts 1 and 2 only contain digits after comma separation
		boolean partOneCheck = isAllDigits(substringSections[0]);
		boolean partTwoCheck = isAllDigits(substringSections[1]);
		// substring valid if both parts return true
		return partOneCheck && partTwoCheck;
	}

	// check if input is only digits
	public static boolean isAllDigits(String substring) {
		for (char ch : substring.toCharArray()) {
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
		return true;
	}

	// return list in which each parsed line from input file is stored
	public static List<String> processLinesFromFile(String inputPath) {
		List<String> lines = new ArrayList<>();
		try {
			File file = new File(inputPath);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line);
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}
		return lines;
	}

	// run solution for day 3 problem
	public static void runDay3() {
		List<String> lines = processLinesFromFile("day3.txt");
		if (lines.isEmpty()) {
			return;
		}
		System.out.println("Sum of all pattern products: " + calculateSumOfAllPatternLines(lines));
		System.out.println("Sum of all pattern products with do() don't() condition: " + calculateSumOfAllPatternLinesWithCondition(lines));

	}
}
