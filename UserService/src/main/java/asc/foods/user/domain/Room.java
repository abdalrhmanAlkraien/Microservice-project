package asc.foods.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_sender")
    private Long userSender;

    @Column(name = "user_reciver")
    private Long userReciver;

    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "readBies", "room" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "room", "appUser" }, allowSetters = true)
    private Set<UserAndRoom> userAndRooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserSender() {
        return this.userSender;
    }

    public Room userSender(Long userSender) {
        this.setUserSender(userSender);
        return this;
    }

    public void setUserSender(Long userSender) {
        this.userSender = userSender;
    }

    public Long getUserReciver() {
        return this.userReciver;
    }

    public Room userReciver(Long userReciver) {
        this.setUserReciver(userReciver);
        return this;
    }

    public void setUserReciver(Long userReciver) {
        this.userReciver = userReciver;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setRoom(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setRoom(this));
        }
        this.messages = messages;
    }

    public Room messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Room addMessage(Message message) {
        this.messages.add(message);
        message.setRoom(this);
        return this;
    }

    public Room removeMessage(Message message) {
        this.messages.remove(message);
        message.setRoom(null);
        return this;
    }

    public Set<UserAndRoom> getUserAndRooms() {
        return this.userAndRooms;
    }

    public void setUserAndRooms(Set<UserAndRoom> userAndRooms) {
        if (this.userAndRooms != null) {
            this.userAndRooms.forEach(i -> i.setRoom(null));
        }
        if (userAndRooms != null) {
            userAndRooms.forEach(i -> i.setRoom(this));
        }
        this.userAndRooms = userAndRooms;
    }

    public Room userAndRooms(Set<UserAndRoom> userAndRooms) {
        this.setUserAndRooms(userAndRooms);
        return this;
    }

    public Room addUserAndRoom(UserAndRoom userAndRoom) {
        this.userAndRooms.add(userAndRoom);
        userAndRoom.setRoom(this);
        return this;
    }

    public Room removeUserAndRoom(UserAndRoom userAndRoom) {
        this.userAndRooms.remove(userAndRoom);
        userAndRoom.setRoom(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", userSender=" + getUserSender() +
            ", userReciver=" + getUserReciver() +
            "}";
    }
}
