package vn.iotstar.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.iotstar.util.VideoUtil;

@Entity
@Table(name = "Videos")
@NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v")
@Getter
@Setter
@NoArgsConstructor
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "videoId")
    private String videoId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX) NULL")
    private String description;

    @Column(name = "poster", columnDefinition = "NVARCHAR(255) NULL")
    private String poster;

    @Column(name = "title", columnDefinition = "NVARCHAR(255) NULL")
    private String title;

    @Column(name = "views")
    private int views;

    /** Link YouTube hoặc link trực tiếp tới file .mp4/.webm/.ogg. */
    @Column(name = "url", columnDefinition = "NVARCHAR(500) NULL")
    private String url;

    @Column(name = "createddate")
    private LocalDateTime createdDate;

    // bi-directional many-to-one association to Category
    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;

    // bi-directional many-to-one association to User (1 User - n Video)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ===== Các getter phụ cho JSP (không ánh xạ xuống cột; entity dùng field access) =====

    /** Id video YouTube (11 ký tự) nếu url là link YouTube, ngược lại null. */
    public String getYoutubeId() {
        return VideoUtil.extractYoutubeId(url);
    }

    /** true nếu url là link trực tiếp tới file video (.mp4/.webm/.ogg). */
    public boolean isDirectMedia() {
        return VideoUtil.isDirectMedia(url);
    }

    public String getCreatedText() {
        return createdDate == null ? "" : createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
