package asc.foods.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "comment_time")
    private Instant commentTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "replay")
    private String replay;

    @OneToMany(mappedBy = "comment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "post", "comment", "story" }, allowSetters = true)
    private Set<PostMultimedia> postMultimedias = new HashSet<>();

    @OneToMany(mappedBy = "comment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "comment" }, allowSetters = true)
    private Set<LikeComment> likeComments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_comment__replay",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "replay_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "likeComments", "replays", "appUser", "post", "replayOfs" }, allowSetters = true)
    private Set<Comment> replays = new HashSet<>();

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

    @ManyToMany(mappedBy = "replays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "likeComments", "replays", "appUser", "post", "replayOfs" }, allowSetters = true)
    private Set<Comment> replayOfs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Comment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Comment text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCommentTime() {
        return this.commentTime;
    }

    public Comment commentTime(Instant commentTime) {
        this.setCommentTime(commentTime);
        return this;
    }

    public void setCommentTime(Instant commentTime) {
        this.commentTime = commentTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Comment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Comment imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReplay() {
        return this.replay;
    }

    public Comment replay(String replay) {
        this.setReplay(replay);
        return this;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public Set<PostMultimedia> getPostMultimedias() {
        return this.postMultimedias;
    }

    public void setPostMultimedias(Set<PostMultimedia> postMultimedias) {
        if (this.postMultimedias != null) {
            this.postMultimedias.forEach(i -> i.setComment(null));
        }
        if (postMultimedias != null) {
            postMultimedias.forEach(i -> i.setComment(this));
        }
        this.postMultimedias = postMultimedias;
    }

    public Comment postMultimedias(Set<PostMultimedia> postMultimedias) {
        this.setPostMultimedias(postMultimedias);
        return this;
    }

    public Comment addPostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.add(postMultimedia);
        postMultimedia.setComment(this);
        return this;
    }

    public Comment removePostMultimedia(PostMultimedia postMultimedia) {
        this.postMultimedias.remove(postMultimedia);
        postMultimedia.setComment(null);
        return this;
    }

    public Set<LikeComment> getLikeComments() {
        return this.likeComments;
    }

    public void setLikeComments(Set<LikeComment> likeComments) {
        if (this.likeComments != null) {
            this.likeComments.forEach(i -> i.setComment(null));
        }
        if (likeComments != null) {
            likeComments.forEach(i -> i.setComment(this));
        }
        this.likeComments = likeComments;
    }

    public Comment likeComments(Set<LikeComment> likeComments) {
        this.setLikeComments(likeComments);
        return this;
    }

    public Comment addLikeComment(LikeComment likeComment) {
        this.likeComments.add(likeComment);
        likeComment.setComment(this);
        return this;
    }

    public Comment removeLikeComment(LikeComment likeComment) {
        this.likeComments.remove(likeComment);
        likeComment.setComment(null);
        return this;
    }

    public Set<Comment> getReplays() {
        return this.replays;
    }

    public void setReplays(Set<Comment> comments) {
        this.replays = comments;
    }

    public Comment replays(Set<Comment> comments) {
        this.setReplays(comments);
        return this;
    }

    public Comment addReplay(Comment comment) {
        this.replays.add(comment);
        comment.getReplayOfs().add(this);
        return this;
    }

    public Comment removeReplay(Comment comment) {
        this.replays.remove(comment);
        comment.getReplayOfs().remove(this);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Comment appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment post(Post post) {
        this.setPost(post);
        return this;
    }

    public Set<Comment> getReplayOfs() {
        return this.replayOfs;
    }

    public void setReplayOfs(Set<Comment> comments) {
        if (this.replayOfs != null) {
            this.replayOfs.forEach(i -> i.removeReplay(this));
        }
        if (comments != null) {
            comments.forEach(i -> i.addReplay(this));
        }
        this.replayOfs = comments;
    }

    public Comment replayOfs(Set<Comment> comments) {
        this.setReplayOfs(comments);
        return this;
    }

    public Comment addReplayOf(Comment comment) {
        this.replayOfs.add(comment);
        comment.getReplays().add(this);
        return this;
    }

    public Comment removeReplayOf(Comment comment) {
        this.replayOfs.remove(comment);
        comment.getReplays().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", commentTime='" + getCommentTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", replay='" + getReplay() + "'" +
            "}";
    }
}
