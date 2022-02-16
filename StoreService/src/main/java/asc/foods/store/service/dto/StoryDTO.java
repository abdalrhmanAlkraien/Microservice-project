package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.MultiMedia;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.store.domain.Story} entity.
 */
public class StoryDTO implements Serializable {

    private Long id;

    private String savedBy;

    private Instant savedDate;

    private Instant endDate;

    private String storyText;

    private MultiMedia multiMedia;

    private String url;

    private String description;

    private AscStoreDTO store;

    private AppUserDTO appUser;

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

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public MultiMedia getMultiMedia() {
        return multiMedia;
    }

    public void setMultiMedia(MultiMedia multiMedia) {
        this.multiMedia = multiMedia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AscStoreDTO getStore() {
        return store;
    }

    public void setStore(AscStoreDTO store) {
        this.store = store;
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
        if (!(o instanceof StoryDTO)) {
            return false;
        }

        StoryDTO storyDTO = (StoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoryDTO{" +
            "id=" + getId() +
            ", savedBy='" + getSavedBy() + "'" +
            ", savedDate='" + getSavedDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", storyText='" + getStoryText() + "'" +
            ", multiMedia='" + getMultiMedia() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", store=" + getStore() +
            ", appUser=" + getAppUser() +
            "}";
    }
}
