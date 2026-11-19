package vn.iotstar.util;

public class Constant {
    /** Thư mục lưu ảnh upload. Đổi lại nếu cần, ví dụ "E:\\upload". */
    public static final String DIR = System.getProperty("D:\\LapTrinhWeb\\Anh_JPAShoppingMVC") + java.io.File.separator + "upload";
    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";
    public static final String DEFAULT_ICON = "avatar.png";
    /** Số video mỗi trang ở /video. */
    public static final int VIDEO_PAGE_SIZE = 6;
    /** Số video mới nhất hiển thị ở trang chủ. */
    public static final int HOME_VIDEO_LIMIT = 10;

    private Constant() {}
}
