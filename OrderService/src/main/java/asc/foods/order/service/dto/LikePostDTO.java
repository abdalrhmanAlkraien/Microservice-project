package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.LikeReactive;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.LikePost} entity.
 */
public class LikePostDTO implements Serializable {

    private Long id;

    private Instant likeDate;

    private String createdBy;

    private LikeReactive likeReactive;

    private PostDTO post;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Instant likeDate) {
        this.likeDate = likeDate;
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

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikePostDTO)) {
            return false;
        }

        LikePostDTO likePostDTO = (LikePostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likePostDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikePostDTO{" +
            "id=" + getId() +
            ", likeDate='" + getLikeDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", likeReactive='" + getLikeReactive() + "'" +
            ", post=" + getPost() +
            ", appUser=" + getAppUser() +
            "}";
    }
}
