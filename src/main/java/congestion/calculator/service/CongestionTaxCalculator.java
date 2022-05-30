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

public class CongestionTaxCalculator {

    private static final Map<String, Integer> tollFreeVehicles = new HashMap<>();

    static {
        tollFreeVehicles.put("Motorcycle", 0);
        tollFreeVehicles.put("Tractor", 1);
        tollFreeVehicles.put("Emergency", 2);
        tollFreeVehicles.put("Diplomat", 3);
        tollFreeVehicles.put("Foreign", 4);
        tollFreeVehicles.put("Military", 5);

    }

    public static int getTax(final List<CongestionTax> congestionTaxList) {
        return congestionTaxList.stream()
                                .mapToInt(value -> getTollFee(value.getLocalDateTime(), value.getVehicle()))
                                .sum();
    }
//    public int getTax(final Vehicle vehicle, final Date[] dates) {
//        final Date intervalStart = dates[0];
//        int totalFee = 0;
//
//        for (int i = 0; i < dates.length; i++) {
//            final Date date = dates[i];
//            final int nextFee = getTollFee(date, vehicle);
//            int tempFee = getTollFee(intervalStart, vehicle);
//
//            final long diffInMillies = date.getTime() - intervalStart.getTime();
//            final long minutes = diffInMillies / 1000 / 60;
//
//            if (minutes <= 60) {
//                if (totalFee > 0)
//                    totalFee -= tempFee;
//                if (nextFee >= tempFee)
//                    tempFee = nextFee;
//                totalFee += tempFee;
//            } else {
//                totalFee += nextFee;
//            }
//        }
//
//        if (totalFee > 60)
//            totalFee = 60;
//        return totalFee;
//    }

    private static boolean isTollFreeVehicle(final Vehicle vehicle) {
        boolean result = false;
        if (vehicle != null) {
            final String vehicleType = vehicle.getVehicleType();
            result = tollFreeVehicles.containsKey(vehicleType);
        }
        return result;
    }

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
//    public int getTollFee(final Date date, final Vehicle vehicle) {
//        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle))
//            return 0;
//
//        final int hour = date.getHours();
//        final int minute = date.getMinutes();
//
//        if (hour == 6 && minute <= 29)
//            return 8;
//        else if (hour == 6 && minute <= 59)
//            return 13;
//        else if (hour == 7 && minute <= 59)
//            return 18;
//        else if (hour == 8 && minute <= 29)
//            return 13;
//        else if (hour >= 8 && hour <= 14 && minute >= 30)
//            return 8;
//        else if (hour == 15 && minute <= 29)
//            return 13;
//        else if (hour == 15 || hour == 16)
//            return 18;
//        else if (hour == 17)
//            return 13;
//        else if (hour == 18 && minute <= 29)
//            return 8;
//        else
//            return 0;
//    }

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
//    private Boolean isTollFreeDate(final Date date) {
//        final int year = date.getYear();
//        final int month = date.getMonth() + 1;
//        final int day = date.getDay() + 1;
//        final int dayOfMonth = date.getDate();
//
//        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY)
//            return true;
//
//        if (year == 2013) {
//            return (month == 1 && dayOfMonth == 1) ||
//                    (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
//                    (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
//                    (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
//                    (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
//                    (month == 7) ||
//                    (month == 11 && dayOfMonth == 1) ||
//                    (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31));
//        }
//        return false;
//    }
}
