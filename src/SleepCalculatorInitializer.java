import javax.swing.*;

public class SleepCalculatorInitializer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SleepCalculatorGUI();
            }
        });
    }
}
