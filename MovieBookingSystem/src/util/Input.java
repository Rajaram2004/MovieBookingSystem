package util;

import java.util.Scanner;

public class Input {
	private static final Scanner SCANNER = new Scanner(System.in);

	private Input() {
	}

	public static Scanner getScanner() {
		return SCANNER;
	}

	public static void closeScanner() {
		SCANNER.close();
	}
	public static int getInteger(int maxValue) {
		Scanner sc = Input.getScanner();
		String input = sc.nextLine();
		try {
			int num = Integer.parseInt(input);
			if (num > 0 && num <=maxValue) {
				return num;

			} else {
				System.err.println("Please Enter a Valid Number.");
				return getInteger(maxValue);
			}
		} catch (NumberFormatException e) {
			System.err.println("Not an integer.");
			return getInteger(maxValue);
		}
	}
	
	public static long getLong(Long maxValue) {
        while (true) {
            try {
                String input = SCANNER.nextLine().trim();
                Long num =  Long.parseLong(input);
                if(num >0 && num <= maxValue) {
                	return num;
                }
                	
            } catch (NumberFormatException e) {
                System.err.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
