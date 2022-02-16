package asc.foods.order.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link asc.foods.order.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    private String text;

    private Instant commentTime;

    private String createdBy;

    private String imageUrl;

    private String replay;

    private Set<CommentDTO> replays = new HashSet<>();

    private AppUserDTO appUser;

    private PostDTO post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Instant commentTime) {
        this.commentTime = commentTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public Set<CommentDTO> getReplays() {
        return replays;
    }

    public void setReplays(Set<CommentDTO> replays) {
        this.replays = replays;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", commentTime='" + getCommentTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", replay='" + getReplay() + "'" +
            ", replays=" + getReplays() +
            ", appUser=" + getAppUser() +
            ", post=" + getPost() +
            "}";
    }
}
