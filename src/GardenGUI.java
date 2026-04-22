import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import OriginalPart.PlantGarden;

public class GardenGUI extends JFrame {

    private final JButton[] plotButtons = new JButton[5];
    private final JTextArea logArea;

    public GardenGUI() {
        setTitle("Virtual Garden Simulator");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        DigitalGarden.initializeGarden();

       //Garden panel buttons
        JPanel gardenPanel = new JPanel();
        gardenPanel.setLayout(new GridLayout(1, 5));

        for (int i = 0; i < 5; i++) {
            plotButtons[i] = new JButton("Empty");
            final int plotIndex = i;
            plotButtons[i].addActionListener(event-> handlePlotClick(plotIndex));
            gardenPanel.add(plotButtons[i]);
        }

        add(gardenPanel, BorderLayout.CENTER);

        //control panel buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 5));

        JButton plantButton = new JButton("Plant");
        JButton waterButton = new JButton("Water");
        JButton harvestButton = new JButton("Harvest");
        JButton endDayButton = new JButton("End Day");
        JButton inventoryButton = new JButton("Inventory");

        controlPanel.add(plantButton);
        controlPanel.add(waterButton);
        controlPanel.add(harvestButton);
        controlPanel.add(endDayButton);
        controlPanel.add(inventoryButton);

        add(controlPanel, BorderLayout.SOUTH);

        //logs
        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.EAST);

        //button actions
        plantButton.addActionListener(event -> showPlantMenu());
        waterButton.addActionListener(event -> waterAction());
        harvestButton.addActionListener(event -> harvestAction());
        endDayButton.addActionListener(event -> endDayAction());
        inventoryButton.addActionListener(event-> showInventory());

        refreshGardenDisplay();
        setVisible(true);
    }

    //Actual actions

    private int selectedPlot = -1;

    private void handlePlotClick(int plotIndex) {
        selectedPlot = plotIndex;
        log("Selected plot #" + (plotIndex + 1));
    }

    private void showPlantMenu() {
        if (selectedPlot == -1) {
            log("Select a plot first.");
            return;
        }

        String[] seeds = DigitalGarden.PLANT_TYPES;

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose a seed",
                "Plant Menu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                seeds,
                seeds[0]
        );

        if (choice != null) {
            int seedIndex = java.util.Arrays.asList(seeds).indexOf(choice);
            DigitalGarden.plant(seedIndex, selectedPlot);
            refreshGardenDisplay();
        }
    }

    //Water notification(when last watered)
    private void waterAction() {
        if (selectedPlot == -1) {
            log("Select a plot first.");
            return;
        }

        PlantGarden.Plant p = DigitalGarden.Garden[selectedPlot];

        if (p.name.equals("Empty")) {
            log("Cannot water an empty plot.");
            return;
        }

        DigitalGarden.water(selectedPlot);


        String time = LocalTime.now().withSecond(0).withNano(0).toString();
        log("Watered " + p.name + " on plot #" + (selectedPlot + 1) + " at " + time);

        refreshGardenDisplay();
    }

    private void harvestAction() {
        if (selectedPlot == -1) {
            log("Select a plot first.");
            return;
        }

        DigitalGarden.harvest(selectedPlot);
        refreshGardenDisplay();
    }

    private void endDayAction() {
        DigitalGarden.endDay();
        refreshGardenDisplay();
    }

    private void showInventory() {
        StringBuilder inv = new StringBuilder();

        for (int i = 0; i < DigitalGarden.PLANT_TYPES.length; i++) {
            inv.append(DigitalGarden.PLANT_TYPES[i])
                    .append(": Seeds=")
                    .append(DigitalGarden.Player.seeds[i])
                    .append(", Harvested=")
                    .append(DigitalGarden.Player.harvested[i])
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this, inv.toString(), "Inventory", JOptionPane.INFORMATION_MESSAGE);
    }



    private void refreshGardenDisplay() {
        for (int i = 0; i < 5; i++) {
            PlantGarden.Plant p = DigitalGarden.Garden[i];

            String label = p.name;
            if (!p.name.equals("Empty")) {
                int stageIndex = Math.min(p.age, DigitalGarden.GROWTH_STAGES.length - 1);
                label += " (" + DigitalGarden.GROWTH_STAGES[stageIndex] + ")";
            }

            plotButtons[i].setText(label);
        }
    }

    //log helper
    private void log(String message) {
        logArea.append(message + "\n");
    }


    public static void main(String[] args) {
        new GardenGUI();
    }
}
