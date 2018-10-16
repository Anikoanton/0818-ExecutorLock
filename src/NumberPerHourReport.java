/**
 * Created by 16683189 on 26.09.2018.
 */
public class NumberPerHourReport {

    private static final int HOURS = 24;
    public final int[] numbers = new int[HOURS];

    public int addForHour(int hour)
    {
        return ++numbers[hour];
    }

    public int getNumberForHour(int hour)
    {
        return numbers[hour];
    }

    static NumberPerHourReport combine(NumberPerHourReport ... reports) {
        NumberPerHourReport result = new NumberPerHourReport();
        for (NumberPerHourReport report : reports) {
            for (int i = 0; i < HOURS; i++) {
                result.numbers[i] += report.numbers[i];
            }
        }
        return result;
    }

}
