package asc.foods.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ViwedStory.
 */
@Entity
@Table(name = "viwed_story")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ViwedStory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "viewd_tine")
    private Instant viewdTine;

    @ManyToMany(mappedBy = "viwedStories")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<AppUser> appUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ViwedStory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getViewdTine() {
        return this.viewdTine;
    }

    public ViwedStory viewdTine(Instant viewdTine) {
        this.setViewdTine(viewdTine);
        return this;
    }

    public void setViewdTine(Instant viewdTine) {
        this.viewdTine = viewdTine;
    }

    public Set<AppUser> getAppUsers() {
        return this.appUsers;
    }

    public void setAppUsers(Set<AppUser> appUsers) {
        if (this.appUsers != null) {
            this.appUsers.forEach(i -> i.removeViwedStory(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addViwedStory(this));
        }
        this.appUsers = appUsers;
    }

    public ViwedStory appUsers(Set<AppUser> appUsers) {
        this.setAppUsers(appUsers);
        return this;
    }

    public ViwedStory addAppUser(AppUser appUser) {
        this.appUsers.add(appUser);
        appUser.getViwedStories().add(this);
        return this;
    }

    public ViwedStory removeAppUser(AppUser appUser) {
        this.appUsers.remove(appUser);
        appUser.getViwedStories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViwedStory)) {
            return false;
        }
        return id != null && id.equals(((ViwedStory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViwedStory{" +
            "id=" + getId() +
            ", viewdTine='" + getViewdTine() + "'" +
            "}";
    }
}
