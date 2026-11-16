package vn.iotstar.util;

import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private static final int COST = 12;
    // Dạng hash BCrypt: $2a$12$ + 53 ký tự
    private static final Pattern BCRYPT = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");

    public static String hash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt(COST));
    }

    public static boolean isHashed(String stored) {
        return stored != null && BCRYPT.matcher(stored).matches();
    }

    public static boolean verify(String plain, String hashed) {
        try {
            return BCrypt.checkpw(plain, hashed);
        } catch (IllegalArgumentException e) {
            return false; // hash không hợp lệ
        }
    }
}