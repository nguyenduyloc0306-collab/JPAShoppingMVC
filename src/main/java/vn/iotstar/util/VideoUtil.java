package vn.iotstar.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Nhận diện nguồn video: link YouTube hoặc link trực tiếp tới file video. */
public final class VideoUtil {
    private VideoUtil() {}

    // youtube.com/watch?v=ID, youtube.com/embed/ID, /shorts/ID, /live/ID, youtu.be/ID
    private static final Pattern YOUTUBE = Pattern.compile(
            "^(?:https?://)?(?:www\\.|m\\.)?(?:youtube\\.com/(?:watch\\?(?:[^#\\s]*&)?v=|embed/|shorts/|live/)"
                    + "|youtu\\.be/)([A-Za-z0-9_-]{11})(?:[?&#/]\\S*)?$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern DIRECT = Pattern.compile(
            "^https?://\\S+\\.(?:mp4|webm|ogg)(?:[?#]\\S*)?$", Pattern.CASE_INSENSITIVE);

    public static String extractYoutubeId(String url) {
        if (url == null) {
            return null;
        }
        Matcher m = YOUTUBE.matcher(url.trim());
        return m.matches() ? m.group(1) : null;
    }

    private static final Pattern LOCAL_VIDEO = Pattern.compile(
            "^[A-Za-z0-9._-]+\\.(?:mp4|webm|ogg|mov)$", Pattern.CASE_INSENSITIVE);

    public static boolean isDirectMedia(String url) {
        if (url == null) {
            return false;
        }
        String trimmed = url.trim();
        return DIRECT.matcher(trimmed).matches() || LOCAL_VIDEO.matcher(trimmed).matches();
    }
    public static boolean isSupported(String url) {
        return extractYoutubeId(url) != null || isDirectMedia(url);
    }
}
