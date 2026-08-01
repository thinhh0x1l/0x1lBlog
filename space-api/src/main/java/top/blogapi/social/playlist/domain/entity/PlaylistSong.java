package top.blogapi.social.playlist.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "playlist_songs")
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "playlist_id", nullable = false)
    Long playlistId;

    @Column(name = "added_by", nullable = false)
    Long addedBy;

    @Column(nullable = false, length = 200)
    String title;

    @Column(length = 200)
    String artist;

    @Column(nullable = false, length = 50)
    String source;

    @Column(nullable = false, length = 255)
    String sourceId;

    @Column(length = 500)
    String thumbnailUrl;

    Integer durationSec;

    Integer sortOrder;

    int voteCount;

    Instant createdAt;
}
