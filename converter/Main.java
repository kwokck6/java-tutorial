package converter;

import java.util.Scanner;
import java.math.*;

public class Main {
    public static void main(String[] args) {
        String numberOrCommand;
        BigInteger srcNum;
        Scanner scanner = new Scanner(System.in);
        int source;
        int target;
        System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
        String command = scanner.nextLine();
        if (!command.equals("/exit")) {
            source = Integer.parseInt(command.split("\\s+")[0]);
            target = Integer.parseInt(command.split("\\s+")[1]);
            while (true) {
                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", source, target);
                numberOrCommand = scanner.nextLine();
                if (!numberOrCommand.equals("/back")) {
                    srcNum = new BigInteger(numberOrCommand, source);
                    System.out.printf("Conversion result: %s%n%n", srcNum.toString(target));
                } else {
                    System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
                    command = scanner.nextLine();
                    if (!command.equals("/exit")) {
                        source = Integer.parseInt(command.split("\\s+")[0]);
                        target = Integer.parseInt(command.split("\\s+")[1]);
                    } else {
                        break;
                    }
                }
            }
        }
    }
}
