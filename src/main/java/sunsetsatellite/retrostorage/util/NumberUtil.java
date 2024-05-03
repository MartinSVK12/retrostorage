package sunsetsatellite.retrostorage.util;

public class NumberUtil {
    public static String[] shortSuffixes = new String[]{"k","M","B","T"};
    public static String[] longSuffixes = new String[]{"thousand","million","billion","trillion"};

    public static String format(double value){
        if(Double.isInfinite(value)){
            return "inf";
        }
        for (String shortSuffix : shortSuffixes) {
            if (value >= 1000) {
                value /= 1000;
                if (value < 1000) {
                    return String.format("%.1f%s", value, shortSuffix);
                }
            } else {
                return String.valueOf((int)value);
            }
        }
        return String.valueOf((int)value);
    }
}
