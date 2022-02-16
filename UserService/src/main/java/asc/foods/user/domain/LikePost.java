package asc.foods.user.domain;

import asc.foods.user.domain.enumeration.LikeReactive;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LikePost.
 */
@Entity
@Table(name = "like_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LikePost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "like_date")
    private Instant likeDate;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_reactive")
    private LikeReactive likeReactive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "likePosts", "postMultimedias", "comments", "saves", "appUser" }, allowSetters = true)
    private Post post;

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

    public LikePost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLikeDate() {
        return this.likeDate;
    }

    public LikePost likeDate(Instant likeDate) {
        this.setLikeDate(likeDate);
        return this;
    }

    public void setLikeDate(Instant likeDate) {
        this.likeDate = likeDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LikePost createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LikeReactive getLikeReactive() {
        return this.likeReactive;
    }

    public LikePost likeReactive(LikeReactive likeReactive) {
        this.setLikeReactive(likeReactive);
        return this;
    }

    public void setLikeReactive(LikeReactive likeReactive) {
        this.likeReactive = likeReactive;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LikePost post(Post post) {
        this.setPost(post);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public LikePost appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikePost)) {
            return false;
        }
        return id != null && id.equals(((LikePost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikePost{" +
            "id=" + getId() +
            ", likeDate='" + getLikeDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", likeReactive='" + getLikeReactive() + "'" +
            "}";
    }
}
