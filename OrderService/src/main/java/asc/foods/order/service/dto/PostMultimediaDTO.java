package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.MultiMedia;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.PostMultimedia} entity.
 */
public class PostMultimediaDTO implements Serializable {

    private Long id;

    private String caption;

    private String url;

    private MultiMedia multiMedia;

    private PostDTO post;

    private CommentDTO comment;

    private StoryDTO story;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MultiMedia getMultiMedia() {
        return multiMedia;
    }

    public void setMultiMedia(MultiMedia multiMedia) {
        this.multiMedia = multiMedia;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public StoryDTO getStory() {
        return story;
    }

    public void setStory(StoryDTO story) {
        this.story = story;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostMultimediaDTO)) {
            return false;
        }

        PostMultimediaDTO postMultimediaDTO = (PostMultimediaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, postMultimediaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostMultimediaDTO{" +
            "id=" + getId() +
            ", caption='" + getCaption() + "'" +
            ", url='" + getUrl() + "'" +
            ", multiMedia='" + getMultiMedia() + "'" +
            ", post=" + getPost() +
            ", comment=" + getComment() +
            ", story=" + getStory() +
            "}";
    }
}
