package asc.foods.user.domain;

import asc.foods.user.domain.enumeration.MultiMedia;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PostMultimedia.
 */
@Entity
@Table(name = "post_multimedia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PostMultimedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "caption")
    private String caption;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "multi_media")
    private MultiMedia multiMedia;

    @ManyToOne
    @JsonIgnoreProperties(value = { "likePosts", "postMultimedias", "comments", "saves", "appUser" }, allowSetters = true)
    private Post post;

    @ManyToOne
    @JsonIgnoreProperties(value = { "postMultimedias", "likeComments", "replays", "appUser", "post", "replayOfs" }, allowSetters = true)
    private Comment comment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "postMultimedias", "store", "appUser" }, allowSetters = true)
    private Story story;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostMultimedia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return this.caption;
    }

    public PostMultimedia caption(String caption) {
        this.setCaption(caption);
        return this;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return this.url;
    }

    public PostMultimedia url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MultiMedia getMultiMedia() {
        return this.multiMedia;
    }

    public PostMultimedia multiMedia(MultiMedia multiMedia) {
        this.setMultiMedia(multiMedia);
        return this;
    }

    public void setMultiMedia(MultiMedia multiMedia) {
        this.multiMedia = multiMedia;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostMultimedia post(Post post) {
        this.setPost(post);
        return this;
    }

    public Comment getComment() {
        return this.comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public PostMultimedia comment(Comment comment) {
        this.setComment(comment);
        return this;
    }

    public Story getStory() {
        return this.story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public PostMultimedia story(Story story) {
        this.setStory(story);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostMultimedia)) {
            return false;
        }
        return id != null && id.equals(((PostMultimedia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostMultimedia{" +
            "id=" + getId() +
            ", caption='" + getCaption() + "'" +
            ", url='" + getUrl() + "'" +
            ", multiMedia='" + getMultiMedia() + "'" +
            "}";
    }
}
