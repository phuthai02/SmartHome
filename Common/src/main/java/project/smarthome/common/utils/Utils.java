package project.smarthome.common.utils;


import io.micrometer.common.util.StringUtils;

public class Utils {

    public static String getAvatarInitials(String fullName) {
        if (StringUtils.isBlank(fullName)) return "NA";
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        int start = Math.max(parts.length - 2, 0);
        StringBuilder initials = new StringBuilder();
        for (int i = start; i < parts.length; i++) {
            initials.append(parts[i].substring(0, 1).toUpperCase());
        }
        return initials.toString();
    }
}
