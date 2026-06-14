package com.verby.indp.domain.creator;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "creator_track")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreatorTrack extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Creator creator;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "stream_url", nullable = false)
    private String streamUrl;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "secs", nullable = false)
    private int secs;

    public CreatorTrack(Creator creator, String filename, String streamUrl, String duration, int secs) {
        validateCreator(creator);
        validateFilename(filename);
        validateStreamUrl(streamUrl);
        this.creator = creator;
        this.filename = filename;
        this.streamUrl = streamUrl;
        this.duration = duration != null ? duration : "0:00";
        this.secs = Math.max(secs, 0);
    }

    private void validateCreator(Creator creator) {
        if (creator == null) {
            throw new BadRequestException("creator는 필수입니다.");
        }
    }

    private void validateFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new BadRequestException("filename은 필수입니다.");
        }
    }

    private void validateStreamUrl(String streamUrl) {
        if (streamUrl == null || streamUrl.isBlank()) {
            throw new BadRequestException("streamUrl은 필수입니다.");
        }
    }
}
