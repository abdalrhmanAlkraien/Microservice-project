package asc.foods.user.domain;

import asc.foods.user.domain.enumeration.MultiMedia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Story.
 */
@Entity
@Table(name = "story")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Story implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "saved_by")
    private String savedBy;

    @Column(name = "saved_date")
    private Instant savedDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "story_text")
    private String storyText;

    @Enumerated(EnumType.STRING)
    @Column(name = "multi_media")
    private MultiMedia multiMedia;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "story")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "post", "comment", "story" }, allowSetters = true)
    private Set<PostMultimedia> postMultimedias = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "branches", "products", "promoCodes", "stories", "followedBys", "productTags", "foodGeners", "store" },
        allowSetters = true
    )
    private AscStore store;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "orders",
            "ratings",
            "userAddresses",
            "follows",
            "userProducts",
            "posts",
            "likePosts",
            "comments",
            "likeComments",
            "saves",
            "stories",
            "userAndRooms",
            "readBies",
            "friends",
            "viwedStories",
            "driver",
            "friendOfs",
        },
        allowSetters = true
    )
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Story id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSavedBy() {
        return this.savedBy;
    }

    public Story savedBy(String savedBy) {
        this.setSavedBy(savedBy);
        return this;
    }

    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    public Instant getSavedDate() {
        return this.savedDate;
    }

    public Story savedDate(Instant savedDate) {
        this.setSavedDate(savedDate);
        return this;
    }

    public void setSavedDate(Instant savedDate) {
        this.savedDate = savedDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Story endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStoryText() {
        return this.storyText;
    }

    public Story storyText(String storyText) {
        this.setStoryText(storyText);
        return this;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public MultiMedia getMultiMedia() {
        return this.multiMedia;
    }

    public Story multiMedia(MultiMedia multiMedia) {
        this.setMultiMedia(multiMedia);
        return this;
    }

    public void setMultiMedia(MultiMedia multiMedia) {
        this.multiMedia = multiMedia;
    }

    public String getUrl() {
        return this.url;
    }

    public Story url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public Story description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PostMultimedia> getPostMultimedias() {
        return this.postMultimedias;
    }

    public void setPostMultimedias(Set<PostMultimedia> postMultimedias) {
        if (this.postMultimedias != null) {
            this.postMultimedias.forEach(i -> i.setStory(null));
        }
        if (postMultimedias != null) {
            postMultimedias.forEach(i -> i.setStory(this));
        }
        this.postMultimedias = postMultimedias;
    }

    public Story postMultimedias(Set<PostMultimedia> postMultimedias) {
        this.setPostMultimedias(postMultimedias);
        return this;
    }

    public Story addPostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.add(postMultimedia);
        postMultimedia.setStory(this);
        return this;
    }

    public Story removePostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.remove(postMultimedia);
        postMultimedia.setStory(null);
        return this;
    }

    public AscStore getStore() {
        return this.store;
    }

    public void setStore(AscStore ascStore) {
        this.store = ascStore;
    }

    public Story store(AscStore ascStore) {
        this.setStore(ascStore);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Story appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Story)) {
            return false;
        }
        return id != null && id.equals(((Story) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Story{" +
            "id=" + getId() +
            ", savedBy='" + getSavedBy() + "'" +
            ", savedDate='" + getSavedDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", storyText='" + getStoryText() + "'" +
            ", multiMedia='" + getMultiMedia() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
