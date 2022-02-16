package asc.foods.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Save.
 */
@Entity
@Table(name = "save")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Save implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "saved_by")
    private String savedBy;

    @Column(name = "saved_date")
    private Instant savedDate;

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "likePosts", "postMultimedias", "comments", "saves", "appUser" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Save id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSavedBy() {
        return this.savedBy;
    }

    public Save savedBy(String savedBy) {
        this.setSavedBy(savedBy);
        return this;
    }

    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    public Instant getSavedDate() {
        return this.savedDate;
    }

    public Save savedDate(Instant savedDate) {
        this.setSavedDate(savedDate);
        return this;
    }

    public void setSavedDate(Instant savedDate) {
        this.savedDate = savedDate;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Save appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Save post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Save)) {
            return false;
        }
        return id != null && id.equals(((Save) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Save{" +
            "id=" + getId() +
            ", savedBy='" + getSavedBy() + "'" +
            ", savedDate='" + getSavedDate() + "'" +
            "}";
    }
}
