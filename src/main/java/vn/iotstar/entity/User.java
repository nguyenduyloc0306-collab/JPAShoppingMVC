package vn.iotstar.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "password")
    private String passWord;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "roleid")
    private int roleid;

    @Column(name = "phone")
    private String phone;

    @Column(name = "createddate")
    private LocalDate createdDate;

    @Column(name = "isActive", nullable = false)
    private boolean active = false;

    @ToString.Exclude
    @Column(name = "otpHash", length = 64)
    private String otpHash;

    @Column(name = "otpExpiry")
    private LocalDateTime otpExpiry;

    @Column(name = "otpAttempts", nullable = false)
    private int otpAttempts = 0;

    @ToString.Exclude
    @Column(name = "resethash", length = 64)
    private String resetHash;

    @Column(name = "resetexpiry")
    private LocalDateTime resetExpiry;

    @Column(name = "resetattempts", nullable = false)
    private int resetAttempts = 0;

    // 1 User - n Video (khóa ngoại user_id nằm ở bảng Videos)
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Video> videos = new ArrayList<>();

    public Video addVideo(Video video) {
        videos.add(video);
        video.setUser(this);
        return video;
    }

    public Video removeVideo(Video video) {
        videos.remove(video);
        video.setUser(null);
        return video;
    }
}
