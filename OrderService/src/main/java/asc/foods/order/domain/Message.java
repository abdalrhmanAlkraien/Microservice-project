package asc.foods.order.domain;

import asc.foods.order.domain.enumeration.AscMediaType;
import asc.foods.order.domain.enumeration.Reaction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "sender")
    private String sender;

    @Column(name = "read_by")
    private Boolean readBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private AscMediaType mediaType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction")
    private Reaction reaction;

    @OneToMany(mappedBy = "message")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "message" }, allowSetters = true)
    private Set<ReadBy> readBies = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "messages", "userAndRooms" }, allowSetters = true)
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Message id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public Message text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Message creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getSender() {
        return this.sender;
    }

    public Message sender(String sender) {
        this.setSender(sender);
        return this;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getReadBy() {
        return this.readBy;
    }

    public Message readBy(Boolean readBy) {
        this.setReadBy(readBy);
        return this;
    }

    public void setReadBy(Boolean readBy) {
        this.readBy = readBy;
    }

    public AscMediaType getMediaType() {
        return this.mediaType;
    }

    public Message mediaType(AscMediaType mediaType) {
        this.setMediaType(mediaType);
        return this;
    }

    public void setMediaType(AscMediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Reaction getReaction() {
        return this.reaction;
    }

    public Message reaction(Reaction reaction) {
        this.setReaction(reaction);
        return this;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
    }

    public Set<ReadBy> getReadBies() {
        return this.readBies;
    }

    public void setReadBies(Set<ReadBy> readBies) {
        if (this.readBies != null) {
            this.readBies.forEach(i -> i.setMessage(null));
        }
        if (readBies != null) {
            readBies.forEach(i -> i.setMessage(this));
        }
        this.readBies = readBies;
    }

    public Message readBies(Set<ReadBy> readBies) {
        this.setReadBies(readBies);
        return this;
    }

    public Message addReadBy(ReadBy readBy) {
        this.readBies.add(readBy);
        readBy.setMessage(this);
        return this;
    }

    public Message removeReadBy(ReadBy readBy) {
        this.readBies.remove(readBy);
        readBy.setMessage(null);
        return this;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Message room(Room room) {
        this.setRoom(room);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", sender='" + getSender() + "'" +
            ", readBy='" + getReadBy() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            ", reaction='" + getReaction() + "'" +
            "}";
    }
}
