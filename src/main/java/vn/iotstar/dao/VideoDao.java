package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Video;

public interface VideoDao {
    void insert(Video video, int userId);
    void update(Video video);
    void delete(String id);
    Video get(String id);
    /** Video đang hiển thị (active), mới nhất trước. */
    List<Video> findLatestActive(int limit);
    /** Video của một user (kể cả video đang ẩn), mới nhất trước. page bắt đầu từ 1. */
    List<Video> findByUser(int userId, int page, int size);
    long countByUser(int userId);
    void increaseViews(String id);
}
