package machine;
import java.util.Scanner;
import java.lang.Math;

public class CoffeeMachine {
    static Scanner scanner = new Scanner(System.in);
    static Action action = Action.valueOf(scanner.next().toUpperCase());

    // initial state
    static int water = 400;
    static int milk = 540;
    static int beans = 120;
    static int cups = 9;
    static int money = 550;

    public static void main(String[] args) {
        // system loop
        System.out.println("Write action (buy, fill, take):");

        while (!action.equals(Action.EXIT)) {
            switch (action) {
                case BUY:
                    buy();
                    break;
                case FILL:
                    fill();
                    break;
                case TAKE:
                    take();
                    break;
                case REMAINING:
                    remaining();
                    break;
            }
            System.out.println("Write action (buy, fill, take):");
            action = Action.valueOf(scanner.next().toUpperCase());
        }
    }

    public static void buy() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:, back - to main menu:");
        String choice = scanner.next();
        String errorMessage = "Sorry, not enough ";
        String successMessage = "I have enough resources, making you a coffee!";
        if (cups < 1) {
            System.out.println(errorMessage + "cups!");
        } else {
            switch (choice) {
                case "1":
                    if (water < 250) {
                        System.out.println(errorMessage + "water!");
                    } else if (beans < 16) {
                        System.out.println(errorMessage + "beans!");
                    } else {
                        System.out.println(successMessage);
                        water -= 250;
                        beans -= 16;
                        money += 4;
                        cups -= 1;
                    }
                    break;
                case "2":
                    if (water < 350) {
                        System.out.println(errorMessage + "water!");
                    } else if (milk < 75) {
                        System.out.println(errorMessage + "milk!");
                    } else if (beans < 20) {
                        System.out.println(errorMessage + "beans!");
                    } else {
                        System.out.println(successMessage);
                        water -= 350;
                        milk -= 75;
                        beans -= 20;
                        money += 7;
                        cups -= 1;
                    }
                    break;
                case "3":
                    if (water < 200) {
                        System.out.println(errorMessage + "water!");
                    } else if (milk < 100) {
                        System.out.println(errorMessage + "milk!");
                    } else if (beans < 12) {
                        System.out.println(errorMessage + "beans!");
                    } else {
                        System.out.println(successMessage);
                        System.out.println();
                        water -= 200;
                        milk -= 100;
                        beans -= 12;
                        money += 6;
                        cups -= 1;
                    }
                    break;
                case "back":
                    break;
            }
        }
    }

    public static void fill() {
        System.out.println("Write how many ml of water you want to add:");
        water += scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        milk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        beans += scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee you want to add:");
        cups += scanner.nextInt();
    }

    public static void take() {
        System.out.printf("I gave you $%d%n", money);
        money = 0;
    }

    public static void remaining() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d ml of water%n", water);
        System.out.printf("%d ml of milk%n", milk);
        System.out.printf("%d g of coffee beans%n", beans);
        System.out.printf("%d disposable cups%n", cups);
        System.out.printf("$%d of money%n", money);
    }
}

enum Action {
    BUY, FILL, TAKE, REMAINING, EXIT
}
