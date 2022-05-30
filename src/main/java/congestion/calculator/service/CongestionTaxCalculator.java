package congestion.calculator.service;

import congestion.calculator.model.CongestionTax;
import congestion.calculator.model.Vehicle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Congestion tax calculator.
 */
public class CongestionTaxCalculator {

    /**
     * The constant tollFreeVehicles.
     */
    private static final Map<String, Integer> tollFreeVehicles = new HashMap<>();
    /**
     * The constant TOTAL_TAX.
     */
    private static final int TOTAL_TAX = 60;

    static {
        tollFreeVehicles.put("Motorcycle", 0);
        tollFreeVehicles.put("Tractor", 1);
        tollFreeVehicles.put("Emergency", 2);
        tollFreeVehicles.put("Diplomat", 3);
        tollFreeVehicles.put("Foreign", 4);
        tollFreeVehicles.put("Military", 5);

    }

    /**
     * Gets tax.
     *
     * @param congestionTaxList the congestion tax list
     * @return the tax
     */
    public static int getTax(final List<CongestionTax> congestionTaxList) {
        final int sum = congestionTaxList.stream()
                                         .mapToInt(value -> getTollFee(value.getLocalDateTime(), value.getVehicle()))
                                         .sum();
        return sum > TOTAL_TAX ? 60: sum;
    }

    /**
     * Is toll free vehicle boolean.
     *
     * @param vehicle the vehicle
     * @return the boolean
     */
    private static boolean isTollFreeVehicle(final Vehicle vehicle) {
        boolean result = false;
        if (vehicle != null) {
            final String vehicleType = vehicle.getVehicleType();
            result = tollFreeVehicles.containsKey(vehicleType);
        }
        return result;
    }

    /**
     * Gets toll fee.
     *
     * @param date    the date
     * @param vehicle the vehicle
     * @return the toll fee
     */
    public static int getTollFee(final LocalDateTime date, final Vehicle vehicle) {
        int result = 13;
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) {
            result = 0;
        } else {
            final int hour = date.getHour();
            final int minute = date.getMinute();
            if (hour == 6 && minute <= 29) {
                result = 8;
            } else if (hour != 6)
                if (hour == 7) {
                    result = 18;
                } else if (hour != 8 || minute > 29)
                    if (hour >= 8 && hour <= 14 && minute >= 30) {
                        result = 8;
                    } else if (hour != 15 || minute > 29)
                        if (hour == 15 || hour == 16) {
                            result = 18;
                        } else if (hour != 17)
                            if (hour == 18 && minute <= 29) {
                                result = 8;
                            } else {
                                result = 0;
                            }
        }

        return result;
    }

    /**
     * Is toll free date boolean.
     *
     * @param date the date
     * @return the boolean
     */
    private static Boolean isTollFreeDate(final LocalDateTime date) {
        boolean result = true;
        if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            final List<LocalDate> tollFreeDates = new ArrayList<>();
            tollFreeDates.add(LocalDate.of(2013, 1, 1));
            tollFreeDates.add(LocalDate.of(2013, 12, 24));
            tollFreeDates.add(LocalDate.of(2013, 12, 25));
            tollFreeDates.add(LocalDate.of(2013, 12, 26));
            tollFreeDates.add(LocalDate.of(2013, 12, 31));
            result = tollFreeDates.stream()
                                  .anyMatch(localDate -> date.toLocalDate()
                                                              .isEqual(localDate));
        }

        return result;
    }
}
