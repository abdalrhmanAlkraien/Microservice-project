package asc.foods.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "decription")
    private String decription;

    @Column(name = "post_time")
    private Instant postTime;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "post", "appUser" }, allowSetters = true)
    private Set<LikePost> likePosts = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "post", "comment", "story" }, allowSetters = true)
    private Set<PostMultimedia> postMultimedias = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "likeComments", "replays", "appUser", "post", "replayOfs" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "post" }, allowSetters = true)
    private Set<Save> saves = new HashSet<>();

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

    public Post id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecription() {
        return this.decription;
    }

    public Post decription(String decription) {
        this.setDecription(decription);
        return this;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Instant getPostTime() {
        return this.postTime;
    }

    public Post postTime(Instant postTime) {
        this.setPostTime(postTime);
        return this;
    }

    public void setPostTime(Instant postTime) {
        this.postTime = postTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Post createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<LikePost> getLikePosts() {
        return this.likePosts;
    }

    public void setLikePosts(Set<LikePost> likePosts) {
        if (this.likePosts != null) {
            this.likePosts.forEach(i -> i.setPost(null));
        }
        if (likePosts != null) {
            likePosts.forEach(i -> i.setPost(this));
        }
        this.likePosts = likePosts;
    }

    public Post likePosts(Set<LikePost> likePosts) {
        this.setLikePosts(likePosts);
        return this;
    }

    public Post addLikePost(LikePost likePost) {
        this.likePosts.add(likePost);
        likePost.setPost(this);
        return this;
    }

    public Post removeLikePost(LikePost likePost) {
        this.likePosts.remove(likePost);
        likePost.setPost(null);
        return this;
    }

    public Set<PostMultimedia> getPostMultimedias() {
        return this.postMultimedias;
    }

    public void setPostMultimedias(Set<PostMultimedia> postMultimedias) {
        if (this.postMultimedias != null) {
            this.postMultimedias.forEach(i -> i.setPost(null));
        }
        if (postMultimedias != null) {
            postMultimedias.forEach(i -> i.setPost(this));
        }
        this.postMultimedias = postMultimedias;
    }

    public Post postMultimedias(Set<PostMultimedia> postMultimedias) {
        this.setPostMultimedias(postMultimedias);
        return this;
    }

    public Post addPostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.add(postMultimedia);
        postMultimedia.setPost(this);
        return this;
    }

    public Post removePostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.remove(postMultimedia);
        postMultimedia.setPost(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPost(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setPost(this));
        }
        this.comments = comments;
    }

    public Post comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Post addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
        return this;
    }

    public Post removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
        return this;
    }

    public Set<Save> getSaves() {
        return this.saves;
    }

    public void setSaves(Set<Save> saves) {
        if (this.saves != null) {
            this.saves.forEach(i -> i.setPost(null));
        }
        if (saves != null) {
            saves.forEach(i -> i.setPost(this));
        }
        this.saves = saves;
    }

    public Post saves(Set<Save> saves) {
        this.setSaves(saves);
        return this;
    }

    public Post addSave(Save save) {
        this.saves.add(save);
        save.setPost(this);
        return this;
    }

    public Post removeSave(Save save) {
        this.saves.remove(save);
        save.setPost(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Post appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", decription='" + getDecription() + "'" +
            ", postTime='" + getPostTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
