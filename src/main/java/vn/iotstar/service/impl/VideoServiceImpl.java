package vn.iotstar.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import vn.iotstar.dao.VideoDao;
import vn.iotstar.dao.impl.VideoDaoImpl;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.util.Constant;
import vn.iotstar.util.UploadUtil;

public class VideoServiceImpl implements VideoService {
    private final VideoDao videoDao = new VideoDaoImpl();

    @Override
    public void insert(Video video, int userId) {
        video.setVideoId(UUID.randomUUID().toString());
        video.setCreatedDate(LocalDateTime.now());
        video.setViews(0);
        videoDao.insert(video, userId);
    }

    @Override
    public boolean edit(Video changes, int userId) {
        Video old = getOwned(changes.getVideoId(), userId);
        if (old == null) {
            return false;
        }
        String oldPoster = old.getPoster();
        old.setTitle(changes.getTitle());
        old.setUrl(changes.getUrl());
        old.setDescription(changes.getDescription());
        old.setActive(changes.isActive());
        old.setCategory(changes.getCategory());
        boolean newPoster = changes.getPoster() != null;
        if (newPoster) {
            old.setPoster(changes.getPoster());
        }
        videoDao.update(old);
        if (newPoster) {
            UploadUtil.deleteImage(oldPoster); // chỉ xóa ảnh cũ sau khi đã lưu DB thành công
        }
        return true;
    }

    @Override
    public boolean delete(String id, int userId) {
        Video old = getOwned(id, userId);
        if (old == null) {
            return false;
        }
        videoDao.delete(id);
        UploadUtil.deleteImage(old.getPoster());
        return true;
    }

    @Override
    public Video get(String id) {
        return videoDao.get(id);
    }

    @Override
    public Video getOwned(String id, int userId) {
        if (id == null || id.isBlank()) {
            return null;
        }
        Video v = videoDao.get(id);
        return (v != null && v.getUser() != null && v.getUser().getId() == userId) ? v : null;
    }

    @Override
    public List<Video> getLatest(int limit) {
        return videoDao.findLatestActive(limit);
    }

    @Override
    public List<Video> getByUser(int userId, int page) {
        return videoDao.findByUser(userId, page, Constant.VIDEO_PAGE_SIZE);
    }

    @Override
    public long countByUser(int userId) {
        return videoDao.countByUser(userId);
    }

    @Override
    public void increaseViews(String id) {
        videoDao.increaseViews(id);
    }
}
