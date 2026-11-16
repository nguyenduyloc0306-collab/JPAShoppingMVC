package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        return String.format("%06d", RANDOM.nextInt(1_000_000)); // String, giữ số 0 đầu
    }

    public static String hash(String otp, String username) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest((username + ":" + otp).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString(); // 64 ký tự hex
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}