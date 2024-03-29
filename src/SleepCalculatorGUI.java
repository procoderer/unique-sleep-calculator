import javax.swing.*;

public class SleepCalculatorGUI {
    private final SleepCalculator sleepCalculator;
    private final JTextField wakeTimeField;
    private final JTextField sleepHoursField;
    private final JTextField bedtimeField;
    private final JTextField newWaketimeField;
    private final JTextArea outputArea;
    private final JPanel panelNewTimes;

    public SleepCalculatorGUI() {
        sleepCalculator = new SleepCalculator();

        // Create the frame
        JFrame frame = new JFrame("Sleep Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 750);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create panels
        JPanel panelChoice = new JPanel();
        JPanel panelTop = new JPanel();
        JPanel panelMiddle = new JPanel();
        JPanel panelBottom = new JPanel();
        panelNewTimes = new JPanel();

        // Initialize fields
        wakeTimeField = new JTextField(5);
        sleepHoursField = new JTextField(2);
        bedtimeField = new JTextField(5);
        newWaketimeField = new JTextField(5);
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        // Choice buttons
        JButton uniqueCycleButton = new JButton("Unique Sleep Cycle");
        JButton defaultCycleButton = new JButton("Default 90-Minute Cycle");
        panelChoice.add(uniqueCycleButton);
        panelChoice.add(defaultCycleButton);

        // Welcome text
        JTextArea welcome = new JTextArea("Welcome to the Sleep Calculator!\n" +
                "Here, your best bedtime can be calculated based on either the standard,\n" +
                "average 90-minute sleep cycle, or your own unique sleep cycle length.\n" +
                "In order to calculate your own unique sleep cycle length, you will need to\n" +
                "provide some of your bedtimes and natural wake times (the times when you wake\n" +
                "up without an alarm). If possible, it is recommended to use your unique sleep\n" +
                "cycle length.");
        welcome.setEditable(false);

        // Labels and Fields
        panelTop.add(welcome);
        panelMiddle.add(new JLabel("Wake Time (24-hour format, XX:XX):"));
        panelMiddle.add(wakeTimeField);
        panelMiddle.add(new JLabel("Sleep Hours:"));
        panelMiddle.add(sleepHoursField);
        panelNewTimes.add(new JLabel("Bedtime:"));
        panelNewTimes.add(bedtimeField);
        panelNewTimes.add(new JLabel("Wake Time:"));
        panelNewTimes.add(newWaketimeField);

        // Time buttons
        JButton calculateButton = new JButton("Calculate Best Bedtimes");
        JButton addTimesButton = new JButton("Add Bedtime and Wake Time");
        JButton clearButton = new JButton("Clear Current Bedtimes and Wake Times");

        // Add components to panels
        panelBottom.add(calculateButton);
        panelBottom.add(new JScrollPane(outputArea));
        panelNewTimes.add(addTimesButton);
        panelNewTimes.add(clearButton);

        // Add panels to frame
        frame.add(panelChoice);
        frame.add(panelTop);
        frame.add(panelMiddle);
        frame.add(panelNewTimes);
        frame.add(panelBottom);

        // Action listeners
        calculateButton.addActionListener(e -> calculateBestTimes());
        addTimesButton.addActionListener(e -> addBedtimeAndWaketime());
        clearButton.addActionListener(e -> sleepCalculator.clearTimes());
        uniqueCycleButton.addActionListener(e -> {
            if (!panelNewTimes.isVisible()) {
                panelNewTimes.setVisible(true);
                outputArea.setText("");
            }
        });
        defaultCycleButton.addActionListener(e -> {
            if (panelNewTimes.isVisible()) {
                panelNewTimes.setVisible(false);
                outputArea.setText("");
            }
        });

        // Initialize with unique cycle option
        panelNewTimes.setVisible(true);

        // Display the frame
        frame.setVisible(true);
    }

    private void calculateBestTimes() {
        String wakeTime = wakeTimeField.getText();
        int sleepHours;
        try {
            sleepHours = Integer.parseInt(sleepHoursField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Please enter a valid number of hours.");
            return;
        }

        // Decide whether to use the unique cycle or default 90 minutes
        if (panelNewTimes.isVisible()) {
            sleepCalculator.updateCycleLength(); // Updates cycle length based on added times
            String[] bedtimes = SleepCalculator.bestTimes(wakeTime, sleepCalculator.getCycleLength(), sleepHours);
            displayBedtimes(bedtimes, true);
        } else {
            String[] bedtimes = SleepCalculator.bestTimes(wakeTime, 90, sleepHours); // Default 90 minutes
            displayBedtimes(bedtimes, false);
        }
    }

    private void displayBedtimes(String[] bedtimes, boolean unique) {
        StringBuilder sb = new StringBuilder();
        if (unique) {
            sb.append("Your unique sleep cycle length is approximately\n" +
                    sleepCalculator.getCycleLength() + " minutes.").append("\n");
        }
        for (String bt : bedtimes) {
            sb.append("Bedtime: " + bt).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void addBedtimeAndWaketime() {
        String bedtime = bedtimeField.getText();
        String waketime = newWaketimeField.getText();
        sleepCalculator.addTimes(bedtime, waketime);
        outputArea.append("Added bedtime: " + bedtime + ", wake time: " + waketime + "\n");
    }
}