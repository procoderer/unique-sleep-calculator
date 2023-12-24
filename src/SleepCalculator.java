import java.util.ArrayList;

public class SleepCalculator {
    private final ArrayList<String> bedtimes; //Strings are in the form of XX:XX, 24-hour time
    private final ArrayList<String> waketimes; //Strings are in the form of XX:XX, 24-hour time
    private int cycleLength; //only an estimation

    public SleepCalculator() {
        bedtimes = new ArrayList<String>();
        waketimes = new ArrayList<String>();
        cycleLength = 90;
    }

    /**
     * Adds a bedtime and a wake time to the respective lists.
     *
     * @param bedtime The bedtime to add in 24-hour format (XX:XX).
     * @param waketime The waketime to add in 24-hour format (XX:XX).
     */
    public void addTimes(String bedtime, String waketime) {
        bedtimes.add(bedtime);
        waketimes.add(waketime);
    }

    /**
     * Clears all the current bedtimes and wake times from their respective lists.
     */
    public void clearTimes() {
        bedtimes.clear();
        waketimes.clear();
    }

    /**
     * Returns the current estimated sleep cycle length in minutes.
     *
     * @return The estimated sleep cycle length.
     */
    public int getCycleLength() {
        return cycleLength;
    }

    /**
     * Updates the estimated sleep cycle length based on the stored bedtimes and waketimes.
     * It calculates the cycle length that minimizes the average decimal difference
     * between actual sleep lengths and multiples of the cycle length.
     */
    public void updateCycleLength() {
        double mostAccurate = avgDecimalDifference(90);
        cycleLength = 90;
        for(int i = 1; i <= 10; i++) {
            if(avgDecimalDifference(90 + i) < mostAccurate) {
                mostAccurate = avgDecimalDifference(90 + i);
                cycleLength = 90 + i;
            }
            if(avgDecimalDifference(90 - i) < mostAccurate) {
                mostAccurate = avgDecimalDifference(90 - i);
                cycleLength = 90 - i;
            }
        }
    }

    /**
     * Calculates the total sleep length in minutes between a given bedtime and waketime.
     *
     * @param bedtime The bedtime in 24-hour format (XX:XX).
     * @param waketime The waketime in 24-hour format (XX:XX).
     * @return The sleep length in minutes.
     */
    public int sleepLength(String bedtime, String waketime) {
        int bedtimeHour = (bedtime.charAt(0) - '0')*10 + (bedtime.charAt(1) - '0');
        int waketimeHour = (waketime.charAt(0) - '0')*10 + (waketime.charAt(1) - '0');
        //this would mean the 23:59 threshold was crossed during sleep
        if(bedtimeHour > waketimeHour) {
            waketimeHour += 24;
        }

        int bedtimeMinutes = bedtimeHour*60 + (bedtime.charAt(3) - '0')*10 + (bedtime.charAt(4) - '0');
        int waketimeMinutes = waketimeHour*60 + (waketime.charAt(3) - '0')*10 + (waketime.charAt(4) - '0');

        return waketimeMinutes - bedtimeMinutes;
    }

    /**
     * Calculates the average decimal part of the quotient when dividing each sleep length
     * by a given cycle length. This is used to assess how well the sleep lengths fit into
     * cycles of the specified length.
     *
     * @param cycle The cycle length in minutes.
     * @return The average decimal difference for the given cycle length.
     */
    public double avgDecimalDifference(int cycle) {
        double decimalSum = 0;

        for(int i = 0; i < bedtimes.size(); i++) {
            double quotient = (double)sleepLength(bedtimes.get(i), waketimes.get(i)) / cycle;
            double decimal = quotient - (int)(quotient);
            //we want decimal difference, not decimal remainder
            if(decimal > 0.5) {
                decimal = 1 - decimal;
            }

            decimalSum += decimal;
        }

        return decimalSum / bedtimes.size();
    }

    /**
     * Calculates and returns an array of the best bedtimes based on a given wake time,
     * sleep cycle, and required hours of sleep. The method calculates 2 or 3 optimal
     * bedtimes by counting backwards from the wake time using the sleep cycle length.
     *
     * @param time The wake time in 24-hour format (XX:XX).
     * @param cycle The sleep cycle length in minutes.
     * @param hours The number of hours of sleep required.
     * @return An array of Strings representing the best bedtimes.
     */
    public static String[] bestTimes(String time, int cycle, int hours) {
        if(hours*60 % cycle == 0) {
            String[] output = new String[3];
            int numCycles = hours*60 / cycle;
            output[0] = getTimeBefore(time, cycle * (numCycles+1));
            output[1] = getTimeBefore(time, cycle * numCycles);
            output[2] = getTimeBefore(time, cycle * (numCycles-1));
            return output;
        }
        else
        {
            String[] output = new String[2];
            int numCycles = hours*60 / cycle;
            output[0] = getTimeBefore(time, cycle * (numCycles+1));
            output[1] = getTimeBefore(time, cycle * numCycles);
            return output;
        }
    }

    /**
     * Calculates and returns a time that is a specified number of minutes before a given time.
     * This method is used to find ideal bedtimes by counting backwards from the wake time.
     *
     * @param time The initial time in 24-hour format (XX:XX).
     * @param minutes The number of minutes to count backwards.
     * @return A String representing the time before the given time.
     */
    public static String getTimeBefore(String time, int minutes) {
        //convert time to number of minutes
        int timeHour = (time.charAt(0) - '0')*10 + (time.charAt(1) - '0');
        int timeMinutes = timeHour*60 + (time.charAt(3) - '0')*10 + (time.charAt(4) - '0');
        //subtract param minutes from the result
        int outputMinutes = timeMinutes - minutes;
        //if outputMinutes crosses the 24-hour barrier, convert it
        if(outputMinutes < 0) {
            outputMinutes = 24*60 + outputMinutes;
        }
        //convert outputMinutes to output String
        int outputHour = outputMinutes / 60;
        int outputMin = outputMinutes % 60;
        String output = outputHour + ":" + outputMin;
        if(outputMin < 10) {
            output = outputHour + ":0" + outputMin;
        }
        if(outputHour < 10) {
            output = "0" + output;
        }

        return output;
    }
}
