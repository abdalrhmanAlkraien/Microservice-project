package asc.foods.order.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.Post} entity.
 */
public class PostDTO implements Serializable {

    private Long id;

    private String decription;

    private Instant postTime;

    private String createdBy;

    private AppUserDTO appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Instant getPostTime() {
        return postTime;
    }

    public void setPostTime(Instant postTime) {
        this.postTime = postTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        if (!(o instanceof PostDTO)) {
            return false;
        }

        PostDTO postDTO = (PostDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostDTO{" +
            "id=" + getId() +
            ", decription='" + getDecription() + "'" +
            ", postTime='" + getPostTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", appUser=" + getAppUser() +
            "}";
    }
}
