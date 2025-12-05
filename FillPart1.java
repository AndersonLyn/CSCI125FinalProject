

import java.util.Scanner;
import java.util.Arrays;

import OriginalPart.*;


public class FillPart1 {

    // Use your partner's PlantGarden classes inside this one (or import if separate)
    public static final String[] PLANT_TYPES = {"Tomato", "Carrot", "Sunflower"};
    public static final String[] GROWTH_STAGES = {"Seed", "Sprout", "Mature", "Harvest Ready"};

    // Garden holds 5 plants
    static PlantGarden.Plant[] Garden = new PlantGarden.Plant[5];

    // Player instance
    static PlantGarden.player Player = new PlantGarden.player();

    static Scanner input = new Scanner(System.in);

    // Initialize garden plots to empty plants
    public static void initializeGarden() {
        for (int i = 0; i < Garden.length; i++) {
            Garden[i] = new PlantGarden.Plant();
            Garden[i].name = "Empty";
            Garden[i].age = 0;
            Garden[i].daysToMature = 3;  // Example default
            Garden[i].watered = false;
            Garden[i].dead = false;
        }
    }

    // Display Main Menu
    public static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1) View Garden");
        System.out.println("2) Plant Seed");
        System.out.println("3) Water Plant");
        System.out.println("4) Harvest Plant");
        System.out.println("5) End Day");
        System.out.println("6) View Inventory");
        System.out.println("0) Quit");
        System.out.print("Choose: ");
    }

    // Display garden with plant names and growth stage (basic)
    public static void displayGarden() {
        System.out.println("\n=== Garden Plots ===");
        for (int i = 0; i < Garden.length; i++) {
            String plantName = Garden[i].name;
            String stage = "N/A";
            if (!plantName.equals("Empty")) {
                // Determine growth stage based on age and daysToMature
                int stageIndex = Math.min(Garden[i].age, GROWTH_STAGES.length - 1);
                stage = GROWTH_STAGES[stageIndex];
            }
            System.out.println((i + 1) + ") " + plantName + " - Stage: " + stage);
        }
    }

    // Display plant types for planting
    public static void displayPlantTypes() {
        System.out.println("\n--- Plant Types ---");
        for (int i = 0; i < PLANT_TYPES.length; i++) {
            System.out.println((i + 1) + ") " + PLANT_TYPES[i]);
        }
        System.out.print("Choose a seed type: ");
    }

    // Ask for plot number from user and return index (0-based)
    public static int askForPlot() {
        System.out.print("Choose a garden plot (1-5): ");
        int choice = input.nextInt();
        return choice - 1;  // Convert to 0-based index
    }

    // Plant a seed if player has seeds and plot is empty
    public static void plant(int seedIndex, int plotIndex) {
        if (plotIndex < 0 || plotIndex >= Garden.length) {
            System.out.println("Invalid plot.");
            return;
        }

        if (!Player.hasSeed(seedIndex)) {
            System.out.println("You don't have any seeds of that type.");
            return;
        }

        if (!Garden[plotIndex].name.equals("Empty")) {
            System.out.println("That plot is already occupied.");
            return;
        }

        Garden[plotIndex].name = PLANT_TYPES[seedIndex];
        Garden[plotIndex].age = 0;
        Garden[plotIndex].daysToMature = 3;  // Set days to mature, can vary per plant
        Garden[plotIndex].watered = false;
        Garden[plotIndex].dead = false;

        Player.useSeed(seedIndex);

        System.out.println("Planted " + PLANT_TYPES[seedIndex] + " in plot #" + (plotIndex + 1));
    }

    // Water a plant if plot is occupied
    public static void water(int plotIndex) {

        //If statement's to verify if the user is able to water the plot
        if (plotIndex < 0 || plotIndex >= Garden.length) {
            System.out.println("Invalid plot.");
            return;
        }
        if (Garden[plotIndex].name.equals("Empty")) {
            System.out.println("Plot is empty. Nothing to water.");
            return;
        }
        Garden[plotIndex].watered = true;
        System.out.println("Watered plot #" + (plotIndex + 1));
    }

    // Harvest a plant if ready
    public static void harvest(int plotIndex) {
        if (plotIndex < 0 || plotIndex >= Garden.length) {
            System.out.println("Invalid plot.");
            return;
        }
        if (Garden[plotIndex].name.equals("Empty")) {
            System.out.println("Plot is empty. Nothing to harvest.");
            return;
        }
        if (Garden[plotIndex].age < Garden[plotIndex].daysToMature) {
            System.out.println("Plant is not ready to harvest.");
            return;
        }
        // Harvest success
        String plantName = Garden[plotIndex].name;
        int seedIndex = Arrays.asList(PLANT_TYPES).indexOf(plantName);
        if (seedIndex != -1) {
            Player.harvestSeed(seedIndex);
        }

        //Default Parameters
        Garden[plotIndex].name = "Empty";
        Garden[plotIndex].age = 0;
        Garden[plotIndex].watered = false;
        Garden[plotIndex].dead = false;

        System.out.println("Harvested " + plantName + " from plot #" + (plotIndex + 1));
    }

    // End day: increment age of watered plants and reset watered status
    public static void endDay() {
        System.out.println("\n--- Day Ended ---");
        for (int i = 0; i < Garden.length; i++) {
            if (!Garden[i].name.equals("Empty") && Garden[i].watered) {
                Garden[i].age++;
                Garden[i].watered = false;
                System.out.println("Plant in plot #" + (i + 1) + " has grown to age " + Garden[i].age);
            } else if (!Garden[i].name.equals("Empty") && !Garden[i].watered) {
                System.out.println("Plant in plot #" + (i + 1) + " did not get watered and did not grow.");
            }
        }
    }

    // Display player inventory
    public static void displayInventory() {
        Player.showInventory();
    }

    // Main program loop
    public static void main(String[] args) {
        System.out.println("Welcome to Virtual Garden Simulator!");

        initializeGarden();

        boolean running = true;

        while (running) {

            //Define integer named "seedChoice" set at value -1
            int seedChoice = -1;

            //Define integer named "plotChoice" set at value -1
            int plotChoice = -1;

            //Display initial menu
            displayMainMenu();

            //Store User input as an integer named "choice"
            int choice = input.nextInt();

            //Switch for Main Menu
            switch (choice) {
                case 1: //Displays the garden
                    displayGarden();
                    break;

                case 2: //Displays garden, asks user for seed choice, asks user for plot choice
                    displayPlantTypes();
                    seedChoice = input.nextInt() - 1;  // convert to 0-based index
                    displayGarden();
                    plotChoice = askForPlot();
                    plant(seedChoice, plotChoice);
                    break;

                case 3: //Displays garden, asks user for plot choice
                    displayGarden();
                    plotChoice = askForPlot();
                    water(plotChoice);
                    break;

                case 4: //Displays garden, asks user for plot choice
                    displayGarden();
                    plotChoice = askForPlot();
                    harvest(plotChoice);
                    break;

                case 5: //Ends day increments Plots values
                    endDay();
                    break;

                case 6: //Displays user's inventory
                    displayInventory();
                    break;

                case 0: //Exits Program
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
