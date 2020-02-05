package edu.wctc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private Scanner keyboard;
    private Cookbook cookbook;

    public Main() throws FileNotFoundException {
        keyboard = new Scanner(System.in);
        cookbook = new Cookbook();

        FileInput indata = new FileInput("meals_data.csv");

        String line;

        System.out.println("Reading in meals information from file...");
        while ((line = indata.fileReadLine()) != null) {
            String[] fields = line.split(",");
            cookbook.addElementWithStrings(fields[0], fields[1], fields[2]);
        }

        runMenu();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new Main();
    }

    private void printMenu() {
        System.out.println("");
        System.out.println("Select Action");
        System.out.println("1. List All Items");
        System.out.println("2. List All Items by Meal");
        System.out.println("3. Search by Meal Name");
        System.out.println("4. Do Control Break");
        System.out.println("5. Exit");
        System.out.print("Please Enter your Choice: ");
    }

    private void runMenu() throws FileNotFoundException {
        boolean userContinue = true;

        while (userContinue) {
            printMenu();

            String ans = keyboard.nextLine();
            switch (ans) {
                case "1":
                    cookbook.printAllMeals();
                    break;
                case "2":
                    listByMealType();
                    break;
                case "3":
                    searchByName();
                    break;
                case "4":
                    doControlBreak();
                    break;
                case "5":
                    userContinue = false;
                    break;
            }
        }

        System.out.println("Goodbye");
        System.exit(0);
    }

    private void listByMealType() {
        // Default value pre-selected in case
        // something goes wrong w/user choice
        MealType mealType = MealType.DINNER;

        System.out.println("Which Meal Type");

        // Generate the menu using the ordinal value of the enum
        for (MealType m : MealType.values()) {
            System.out.println((m.ordinal() + 1) + ". " + m.getMeal());
        }

        System.out.print("Please Enter your Choice: ");
        String ans = keyboard.nextLine();

        try {
            int ansNum = Integer.parseInt(ans);
            if (ansNum < MealType.values().length) {
                mealType = MealType.values()[ansNum - 1];
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Meal Type " + ans + ", defaulted to " + mealType.getMeal() + ".");
        }

        cookbook.printMealsByType(mealType);
    }

    private void searchByName() {
        System.out.print("Please Enter Value: ");
        String ans = keyboard.nextLine();
        cookbook.printByNameSearch(ans);
    }

    private void doControlBreak() throws FileNotFoundException {
        File file = new File("meals_data.csv");
        Scanner sc = new Scanner(file);

        if (file.exists()) {
            System.out.println("Meal Type\tTotal\tMean\tMin\tMax\tMedian");
            ArrayList<Integer> cals = new ArrayList<>();
            String currentMealType = "";
            String nextMealType = null;
            int calTotal = 0;
            int median = (cals.size() / 2);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] meal = line.split(",");
                nextMealType = meal[0];
                String mealCal = meal[2];

                if (nextMealType.equalsIgnoreCase(currentMealType) || currentMealType.equalsIgnoreCase("")) {
                    cals.add(Integer.parseInt(mealCal));
                    if (currentMealType.equalsIgnoreCase("")) {
                        currentMealType = nextMealType;
                    }
                } else {
                    for (int i = 0; i < (cals.size() - 1); i++) {
                        for (int j = 0; j < cals.size() - i - 1; j++) {
                            if (cals.get(j).compareTo(cals.get(j + 1)) > 0) {
                                int temp = cals.get(j);
                                cals.set(j, cals.get(j + 1));
                                cals.set(j + 1, temp);
                            }
                        }
                    }
                    for (int i = 0; i < cals.size(); i++) {
                        calTotal += cals.get(i);
                    }
                    System.out.println(currentMealType + "\t" + calTotal + "\t" + (calTotal / (cals.size())) + "\t" + cals.get(0) + "\t" + cals.get(cals.size() - 1) + "\t" + cals.get(median));
                    currentMealType = nextMealType;
                    cals.clear();
                    cals.add(Integer.parseInt(mealCal));
                }
            }
        }
    }
}

