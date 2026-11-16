package vn.iotstar.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // bi-directional many-to-one association to Category
    @ManyToOne
    @JoinColumn(name = "cate_id")
    private Category category;
}
