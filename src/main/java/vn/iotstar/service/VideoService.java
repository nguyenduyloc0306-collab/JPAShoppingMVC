package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Video;

public interface VideoService {
    /** Gán id, ngày tạo, lượt xem = 0 rồi lưu video cho user userId. */
    void insert(Video video, int userId);
    /** Chỉ chủ sở hữu mới sửa được. poster = null nghĩa là giữ poster cũ. Trả false nếu không có quyền/không tồn tại. */
    boolean edit(Video changes, int userId);
    /** Chỉ chủ sở hữu mới xóa được. Trả false nếu không có quyền/không tồn tại. */
    boolean delete(String id, int userId);
    Video get(String id);
    /** Trả video nếu tồn tại và thuộc về userId, ngược lại null. */
    Video getOwned(String id, int userId);
    List<Video> getLatest(int limit);
    List<Video> getByUser(int userId, int page);
    long countByUser(int userId);
    void increaseViews(String id);
}
