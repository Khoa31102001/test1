package tech.dut.fasto.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.data.domain.Sort;
import tech.dut.fasto.util.constants.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AppUtil {

    private AppUtil() {}

    private static final int EARTH_RADIUS = 6371;

    public static String generateDigitCode() {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        return randomStringGenerator.generate(Constants.DIGIT_CODE_LENGTH);
    }

    public static Double getDistanceFromLatLonInKm(double x1, double y1, double x2, double y2) {
        double dLat = deg2rad(x2 - x1);
        double dLon = deg2rad(y2 - y1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(x1)) * Math.cos(deg2rad(x2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return BigDecimal.valueOf(EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)))).setScale(0, RoundingMode.HALF_UP).doubleValue();
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static Sort buildSortCreated(String sortStr) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        if (StringUtils.isNotEmpty(sortStr)) {
            switch (sortStr) {
                case Constants.SORT_OLDEST_TO_NEWEST:
                    sort = Sort.by(Sort.Order.asc("createdAt"));
                    break;
                case Constants.SORT_NEWEST_TO_OLDEST:
                    sort = Sort.by(Sort.Order.desc("createdAt"));
                    break;
            }
        }
        return sort;
    }

   public static String convertDoublePointToPoint(String point){
        return point.replace(":",".");
   }


}
