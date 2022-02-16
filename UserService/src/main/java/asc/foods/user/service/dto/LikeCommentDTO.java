package asc.foods.user.service.dto;

import asc.foods.user.domain.enumeration.LikeReactive;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.user.domain.LikeComment} entity.
 */
public class LikeCommentDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private String createdBy;

    private LikeReactive likeReactive;

    private AppUserDTO appUser;

    private CommentDTO comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LikeReactive getLikeReactive() {
        return likeReactive;
    }

    public void setLikeReactive(LikeReactive likeReactive) {
        this.likeReactive = likeReactive;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikeCommentDTO)) {
            return false;
        }

        LikeCommentDTO likeCommentDTO = (LikeCommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likeCommentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikeCommentDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", likeReactive='" + getLikeReactive() + "'" +
            ", appUser=" + getAppUser() +
            ", comment=" + getComment() +
            "}";
    }
}
