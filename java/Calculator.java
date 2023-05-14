import java.util.Scanner;

class Calculator {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String equation;
		while (true) {
			System.out.print("Input the equation: ");
			equation = in.nextLine();
			try {
				System.out.println("Result: " + solve(equation));
			} catch (CalculatorException e) {
				System.out.println(e);
				in.close();
				break;
			}
			equation = "";
		}
	}

	static boolean checkPresence(String string, String secondString) {
		for (int i = 0; i < string.length(); i++) {
			if (secondString.contains(String.valueOf(string.charAt(i)))) {
				return true;
			}
		}
		return false;
	}

	static String matches(String string, String secondString) {
		String result = "";
		for (int i = 0; i < string.length(); i++) {
			if (secondString.contains(String.valueOf(string.charAt(i)))) {
				result += String.valueOf(string.charAt(i));
			}
		}
		return result;
	}

	static String checkNumberSystem(String string) throws CalculatorException {
		int arabic = 0;
		int roman = 0;
		String romanString = "IVXLCDM";
		String arabicString = "0123456789";
		for (int i = 0; i < string.length(); i++) {
			if (romanString.contains(String.valueOf(string.charAt(i))) && roman == 0) {
				roman++;
			} else if (arabicString.contains(String.valueOf(string.charAt(i))) && arabic == 0) {
				arabic++;
			}
			if (roman > 0 && arabic > 0) {
				throw new CalculatorException("Different number systems");
			}
		}
		if (roman == 1) {
			return "roman";
		} else {
			return "arabic";
		}
	}

	static String[] toArray(String string, String operation) throws CalculatorException {
		String[] numbers = string.split(operation);
		if (numbers.length < 2) {
			throw new CalculatorException("Missing term");
		}
		String[] result = new String[3];
		result[0] = numbers[0].trim();
		result[1] = numbers[1].trim();
		return result;
	}

	static void checkNumbers(String[] array, String system) throws CalculatorException {
		if (system == "roman") {
			if (checkPresence(array[0], "LCDM")) {
				throw new CalculatorException("Invalid number");
			}
		} else {
			if (array[0].contains(".") || array[0].contains(",") || array[1].contains(".") || array[1].contains(",")) {
				throw new CalculatorException("Fractional number");
			}
			if ((array[0].length() > 1 && String.valueOf(array[0].charAt(0)) == "0") ||
					(array[1].length() > 1 && String.valueOf(array[1].charAt(0)) == "0") ||
					(Integer.valueOf(array[0]) > 10 || Integer.valueOf(array[0]) < 0) ||
					(Integer.valueOf(array[1]) > 10 || Integer.valueOf(array[1]) < 0)) {
				throw new CalculatorException("Invalid number");
			}
		}
	}

	static String toArabic(String string) throws CalculatorException {
		int result = 0;
		int current = 0;
		int last = 0;
		int biggest = 0;
		for (int i = string.length() - 1; i > -1; i--) {
			if (i != string.length() - 1) {
				last = current;
			}
			switch (String.valueOf(string.charAt(i))) {
				case "I":
					current = 1;
					break;
				case "V":
					current = 5;
					break;
				case "X":
					current = 10;
				default:
					break;
			}
			if (last != 0) {
				biggest = current > biggest ? current : biggest;
				if (current == 5 && last > current) {
					throw new CalculatorException("Invalid number");
				} else if (current == 1 && last == current && biggest > current) {
					throw new CalculatorException("Invalid number");
				} else if (current == 5 && last == 10) {
					throw new CalculatorException("Invalid number");
				} else if (current < last) {
					result -= current;
				} else {
					result += current;
				}
			} else {
				biggest = current;
				result = current;
			}
			if (result > 10) {
				throw new CalculatorException("Number greater than 10");
			}
		}
		return String.valueOf(result);
	}

	static String toRoman(int num) throws CalculatorException {
		String input = String.valueOf(num);
		String result = "";
		if (input.contains("-")) {
			throw new CalculatorException("There are no negative numbers in the Roman system");
		}
		for (int i = 0; i < input.length(); i++) {
			if (num == 100) {
				return "C";
			} else if (num == 0) {
				return "N";
			}
			if (input.length() - 1 > i) {
				switch (String.valueOf(input.charAt(i))) {
					case "1":
						result += "X";
						break;
					case "2":
						result += "XX";
						break;
					case "3":
						result += "XXX";
						break;
					case "4":
						result += "XL";
						break;
					case "5":
						result += "L";
						break;
					case "6":
						result += "LX";
						break;
					case "7":
						result += "LXX";
						break;
					case "8":
						result += "LXXX";
						break;
					case "9":
						result += "XC";
						break;
					default:
						break;
				}
			} else {
				switch (String.valueOf(input.charAt(i))) {
					case "1":
						result += "I";
						break;
					case "2":
						result += "II";
						break;
					case "3":
						result += "III";
						break;
					case "4":
						result += "IV";
						break;
					case "5":
						result += "V";
						break;
					case "6":
						result += "VI";
						break;
					case "7":
						result += "VII";
						break;
					case "8":
						result += "VIII";
						break;
					case "9":
						result += "IX";
						break;
					default:
						break;
				}
			}
		}
		return result;
	}

	static String solve(String string) throws CalculatorException {
		string = string.trim();
		String result = "";
		String operation = matches(string, "+-*/");
		String[] numbers;
		String system;
		if (!checkPresence(string, "0123456789IVXLCDM")) {
			throw new CalculatorException("Missing terms");
		}
		if (operation.length() != 1) {
			throw new CalculatorException("Invalid equation");
		} else if (string.contains("+")) {
			operation = "\\+";
		} else if (string.contains("-")) {
			operation = "-";
		} else if (string.contains("*")) {
			operation = "\\*";
		} else if (string.contains("/")) {
			operation = "/";
		}
		numbers = toArray(string, operation);
		system = checkNumberSystem(string);
		checkNumbers(numbers, system);
		if (system == "roman") {
			numbers[0] = toArabic(numbers[0]);
			numbers[1] = toArabic(numbers[1]);
		}
		switch (operation) {
			case "\\+":
				result = String.valueOf(Integer.valueOf(numbers[0]) + Integer.valueOf(numbers[1]));
				break;
			case "-":
				result = String.valueOf(Integer.valueOf(numbers[0]) - Integer.valueOf(numbers[1]));
				break;
			case "\\*":
				result = String.valueOf(Integer.valueOf(numbers[0]) * Integer.valueOf(numbers[1]));
				break;
			case "/":
				result = String.valueOf(Integer.valueOf(numbers[0]) / Integer.valueOf(numbers[1]));
				break;
			default:
				break;
		}
		if (system == "roman") {
			return toRoman(Integer.valueOf(result));
		}
		return result;
	}
}
