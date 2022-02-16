package asc.foods.order.service.dto;

import asc.foods.order.domain.enumeration.AscMediaType;
import asc.foods.order.domain.enumeration.Reaction;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.Message} entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String text;

    private Instant creationDate;

    private String sender;

    private Boolean readBy;

    private AscMediaType mediaType;

    private Reaction reaction;

    private RoomDTO room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getReadBy() {
        return readBy;
    }

    public void setReadBy(Boolean readBy) {
        this.readBy = readBy;
    }

    public AscMediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(AscMediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", sender='" + getSender() + "'" +
            ", readBy='" + getReadBy() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            ", reaction='" + getReaction() + "'" +
            ", room=" + getRoom() +
            "}";
    }
}
