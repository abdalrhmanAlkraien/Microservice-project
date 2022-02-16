package asc.foods.store.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.Save} entity.
 */
public class SaveDTO implements Serializable {

    private Long id;

    private String savedBy;

    private Instant savedDate;

    private AppUserDTO appUser;

    private PostDTO post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSavedBy() {
        return savedBy;
    }

    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    public Instant getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(Instant savedDate) {
        this.savedDate = savedDate;
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
        if (!(o instanceof SaveDTO)) {
            return false;
        }

        SaveDTO saveDTO = (SaveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaveDTO{" +
            "id=" + getId() +
            ", savedBy='" + getSavedBy() + "'" +
            ", savedDate='" + getSavedDate() + "'" +
            ", appUser=" + getAppUser() +
            ", post=" + getPost() +
            "}";
    }
}
