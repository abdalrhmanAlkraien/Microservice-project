package asc.foods.store.domain;

import asc.foods.store.domain.enumeration.Gender;
import asc.foods.store.domain.enumeration.UserStatus;
import asc.foods.store.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "login")
    private String login;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "lang_key")
    private String langKey;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Column(name = "cover_photo")
    private String coverPhoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "dob")
    private String dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "is_hiring")
    private Boolean isHiring;

    @Column(name = "enable_messages")
    private Boolean enableMessages;

    @Column(name = "enable_notifications")
    private Boolean enableNotifications;

    @Column(name = "enable_offers_notifications")
    private Boolean enableOffersNotifications;

    @Column(name = "is_dark")
    private Boolean isDark;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "ratings", "products", "orderCustomes", "store", "userAddress", "appUser", "driver" },
        allowSetters = true
    )
    private Set<AscOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "driver", "order" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orders", "appUser" }, allowSetters = true)
    private Set<UserAddress> userAddresses = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "store", "appUser" }, allowSetters = true)
    private Set<StoreFollower> follows = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "product" }, allowSetters = true)
    private Set<UserProduct> userProducts = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "likePosts", "postMultimedias", "comments", "saves", "appUser" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "post", "appUser" }, allowSetters = true)
    private Set<LikePost> likePosts = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "likeComments", "replays", "appUser", "post", "replayOfs" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "comment" }, allowSetters = true)
    private Set<LikeComment> likeComments = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "post" }, allowSetters = true)
    private Set<Save> saves = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "postMultimedias", "store", "appUser" }, allowSetters = true)
    private Set<Story> stories = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "room", "appUser" }, allowSetters = true)
    private Set<UserAndRoom> userAndRooms = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "message" }, allowSetters = true)
    private Set<ReadBy> readBies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_app_user__friends",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "friends_id")
    )
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
    private Set<AppUser> friends = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_app_user__viwed_story",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "viwed_story_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUsers" }, allowSetters = true)
    private Set<ViwedStory> viwedStories = new HashSet<>();

    @JsonIgnoreProperties(value = { "appUser", "orders", "ratings" }, allowSetters = true)
    @OneToOne(mappedBy = "appUser")
    private Driver driver;

    @ManyToMany(mappedBy = "friends")
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
    private Set<AppUser> friendOfs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AppUser id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public AppUser mobileNumber(String mobileNumber) {
        this.setMobileNumber(mobileNumber);
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLogin() {
        return this.login;
    }

    public AppUser login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public AppUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public AppUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public AppUser activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return this.langKey;
    }

    public AppUser langKey(String langKey) {
        this.setLangKey(langKey);
        return this;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public AppUser imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public AppUser userStatus(UserStatus userStatus) {
        this.setUserStatus(userStatus);
        return this;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getCoverPhoto() {
        return this.coverPhoto;
    }

    public AppUser coverPhoto(String coverPhoto) {
        this.setCoverPhoto(coverPhoto);
        return this;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public AppUser userType(UserType userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDob() {
        return this.dob;
    }

    public AppUser dob(String dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return this.gender;
    }

    public AppUser gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getIsHiring() {
        return this.isHiring;
    }

    public AppUser isHiring(Boolean isHiring) {
        this.setIsHiring(isHiring);
        return this;
    }

    public void setIsHiring(Boolean isHiring) {
        this.isHiring = isHiring;
    }

    public Boolean getEnableMessages() {
        return this.enableMessages;
    }

    public AppUser enableMessages(Boolean enableMessages) {
        this.setEnableMessages(enableMessages);
        return this;
    }

    public void setEnableMessages(Boolean enableMessages) {
        this.enableMessages = enableMessages;
    }

    public Boolean getEnableNotifications() {
        return this.enableNotifications;
    }

    public AppUser enableNotifications(Boolean enableNotifications) {
        this.setEnableNotifications(enableNotifications);
        return this;
    }

    public void setEnableNotifications(Boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public Boolean getEnableOffersNotifications() {
        return this.enableOffersNotifications;
    }

    public AppUser enableOffersNotifications(Boolean enableOffersNotifications) {
        this.setEnableOffersNotifications(enableOffersNotifications);
        return this;
    }

    public void setEnableOffersNotifications(Boolean enableOffersNotifications) {
        this.enableOffersNotifications = enableOffersNotifications;
    }

    public Boolean getIsDark() {
        return this.isDark;
    }

    public AppUser isDark(Boolean isDark) {
        this.setIsDark(isDark);
        return this;
    }

    public void setIsDark(Boolean isDark) {
        this.isDark = isDark;
    }

    public Set<AscOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<AscOrder> ascOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setAppUser(null));
        }
        if (ascOrders != null) {
            ascOrders.forEach(i -> i.setAppUser(this));
        }
        this.orders = ascOrders;
    }

    public AppUser orders(Set<AscOrder> ascOrders) {
        this.setOrders(ascOrders);
        return this;
    }

    public AppUser addOrders(AscOrder ascOrder) {
        this.orders.add(ascOrder);
        ascOrder.setAppUser(this);
        return this;
    }

    public AppUser removeOrders(AscOrder ascOrder) {
        this.orders.remove(ascOrder);
        ascOrder.setAppUser(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setAppUser(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setAppUser(this));
        }
        this.ratings = ratings;
    }

    public AppUser ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public AppUser addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setAppUser(this);
        return this;
    }

    public AppUser removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setAppUser(null);
        return this;
    }

    public Set<UserAddress> getUserAddresses() {
        return this.userAddresses;
    }

    public void setUserAddresses(Set<UserAddress> userAddresses) {
        if (this.userAddresses != null) {
            this.userAddresses.forEach(i -> i.setAppUser(null));
        }
        if (userAddresses != null) {
            userAddresses.forEach(i -> i.setAppUser(this));
        }
        this.userAddresses = userAddresses;
    }

    public AppUser userAddresses(Set<UserAddress> userAddresses) {
        this.setUserAddresses(userAddresses);
        return this;
    }

    public AppUser addUserAddress(UserAddress userAddress) {
        this.userAddresses.add(userAddress);
        userAddress.setAppUser(this);
        return this;
    }

    public AppUser removeUserAddress(UserAddress userAddress) {
        this.userAddresses.remove(userAddress);
        userAddress.setAppUser(null);
        return this;
    }

    public Set<StoreFollower> getFollows() {
        return this.follows;
    }

    public void setFollows(Set<StoreFollower> storeFollowers) {
        if (this.follows != null) {
            this.follows.forEach(i -> i.setAppUser(null));
        }
        if (storeFollowers != null) {
            storeFollowers.forEach(i -> i.setAppUser(this));
        }
        this.follows = storeFollowers;
    }

    public AppUser follows(Set<StoreFollower> storeFollowers) {
        this.setFollows(storeFollowers);
        return this;
    }

    public AppUser addFollows(StoreFollower storeFollower) {
        this.follows.add(storeFollower);
        storeFollower.setAppUser(this);
        return this;
    }

    public AppUser removeFollows(StoreFollower storeFollower) {
        this.follows.remove(storeFollower);
        storeFollower.setAppUser(null);
        return this;
    }

    public Set<UserProduct> getUserProducts() {
        return this.userProducts;
    }

    public void setUserProducts(Set<UserProduct> userProducts) {
        if (this.userProducts != null) {
            this.userProducts.forEach(i -> i.setAppUser(null));
        }
        if (userProducts != null) {
            userProducts.forEach(i -> i.setAppUser(this));
        }
        this.userProducts = userProducts;
    }

    public AppUser userProducts(Set<UserProduct> userProducts) {
        this.setUserProducts(userProducts);
        return this;
    }

    public AppUser addUserProduct(UserProduct userProduct) {
        this.userProducts.add(userProduct);
        userProduct.setAppUser(this);
        return this;
    }

    public AppUser removeUserProduct(UserProduct userProduct) {
        this.userProducts.remove(userProduct);
        userProduct.setAppUser(null);
        return this;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setAppUser(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setAppUser(this));
        }
        this.posts = posts;
    }

    public AppUser posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public AppUser addPost(Post post) {
        this.posts.add(post);
        post.setAppUser(this);
        return this;
    }

    public AppUser removePost(Post post) {
        this.posts.remove(post);
        post.setAppUser(null);
        return this;
    }

    public Set<LikePost> getLikePosts() {
        return this.likePosts;
    }

    public void setLikePosts(Set<LikePost> likePosts) {
        if (this.likePosts != null) {
            this.likePosts.forEach(i -> i.setAppUser(null));
        }
        if (likePosts != null) {
            likePosts.forEach(i -> i.setAppUser(this));
        }
        this.likePosts = likePosts;
    }

    public AppUser likePosts(Set<LikePost> likePosts) {
        this.setLikePosts(likePosts);
        return this;
    }

    public AppUser addLikePost(LikePost likePost) {
        this.likePosts.add(likePost);
        likePost.setAppUser(this);
        return this;
    }

    public AppUser removeLikePost(LikePost likePost) {
        this.likePosts.remove(likePost);
        likePost.setAppUser(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setAppUser(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAppUser(this));
        }
        this.comments = comments;
    }

    public AppUser comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public AppUser addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAppUser(this);
        return this;
    }

    public AppUser removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAppUser(null);
        return this;
    }

    public Set<LikeComment> getLikeComments() {
        return this.likeComments;
    }

    public void setLikeComments(Set<LikeComment> likeComments) {
        if (this.likeComments != null) {
            this.likeComments.forEach(i -> i.setAppUser(null));
        }
        if (likeComments != null) {
            likeComments.forEach(i -> i.setAppUser(this));
        }
        this.likeComments = likeComments;
    }

    public AppUser likeComments(Set<LikeComment> likeComments) {
        this.setLikeComments(likeComments);
        return this;
    }

    public AppUser addLikeComment(LikeComment likeComment) {
        this.likeComments.add(likeComment);
        likeComment.setAppUser(this);
        return this;
    }

    public AppUser removeLikeComment(LikeComment likeComment) {
        this.likeComments.remove(likeComment);
        likeComment.setAppUser(null);
        return this;
    }

    public Set<Save> getSaves() {
        return this.saves;
    }

    public void setSaves(Set<Save> saves) {
        if (this.saves != null) {
            this.saves.forEach(i -> i.setAppUser(null));
        }
        if (saves != null) {
            saves.forEach(i -> i.setAppUser(this));
        }
        this.saves = saves;
    }

    public AppUser saves(Set<Save> saves) {
        this.setSaves(saves);
        return this;
    }

    public AppUser addSave(Save save) {
        this.saves.add(save);
        save.setAppUser(this);
        return this;
    }

    public AppUser removeSave(Save save) {
        this.saves.remove(save);
        save.setAppUser(null);
        return this;
    }

    public Set<Story> getStories() {
        return this.stories;
    }

    public void setStories(Set<Story> stories) {
        if (this.stories != null) {
            this.stories.forEach(i -> i.setAppUser(null));
        }
        if (stories != null) {
            stories.forEach(i -> i.setAppUser(this));
        }
        this.stories = stories;
    }

    public AppUser stories(Set<Story> stories) {
        this.setStories(stories);
        return this;
    }

    public AppUser addStory(Story story) {
        this.stories.add(story);
        story.setAppUser(this);
        return this;
    }

    public AppUser removeStory(Story story) {
        this.stories.remove(story);
        story.setAppUser(null);
        return this;
    }

    public Set<UserAndRoom> getUserAndRooms() {
        return this.userAndRooms;
    }

    public void setUserAndRooms(Set<UserAndRoom> userAndRooms) {
        if (this.userAndRooms != null) {
            this.userAndRooms.forEach(i -> i.setAppUser(null));
        }
        if (userAndRooms != null) {
            userAndRooms.forEach(i -> i.setAppUser(this));
        }
        this.userAndRooms = userAndRooms;
    }

    public AppUser userAndRooms(Set<UserAndRoom> userAndRooms) {
        this.setUserAndRooms(userAndRooms);
        return this;
    }

    public AppUser addUserAndRoom(UserAndRoom userAndRoom) {
        this.userAndRooms.add(userAndRoom);
        userAndRoom.setAppUser(this);
        return this;
    }

    public AppUser removeUserAndRoom(UserAndRoom userAndRoom) {
        this.userAndRooms.remove(userAndRoom);
        userAndRoom.setAppUser(null);
        return this;
    }

    public Set<ReadBy> getReadBies() {
        return this.readBies;
    }

    public void setReadBies(Set<ReadBy> readBies) {
        if (this.readBies != null) {
            this.readBies.forEach(i -> i.setAppUser(null));
        }
        if (readBies != null) {
            readBies.forEach(i -> i.setAppUser(this));
        }
        this.readBies = readBies;
    }

    public AppUser readBies(Set<ReadBy> readBies) {
        this.setReadBies(readBies);
        return this;
    }

    public AppUser addReadBy(ReadBy readBy) {
        this.readBies.add(readBy);
        readBy.setAppUser(this);
        return this;
    }

    public AppUser removeReadBy(ReadBy readBy) {
        this.readBies.remove(readBy);
        readBy.setAppUser(null);
        return this;
    }

    public Set<AppUser> getFriends() {
        return this.friends;
    }

    public void setFriends(Set<AppUser> appUsers) {
        this.friends = appUsers;
    }

    public AppUser friends(Set<AppUser> appUsers) {
        this.setFriends(appUsers);
        return this;
    }

    public AppUser addFriends(AppUser appUser) {
        this.friends.add(appUser);
        appUser.getFriendOfs().add(this);
        return this;
    }

    public AppUser removeFriends(AppUser appUser) {
        this.friends.remove(appUser);
        appUser.getFriendOfs().remove(this);
        return this;
    }

    public Set<ViwedStory> getViwedStories() {
        return this.viwedStories;
    }

    public void setViwedStories(Set<ViwedStory> viwedStories) {
        this.viwedStories = viwedStories;
    }

    public AppUser viwedStories(Set<ViwedStory> viwedStories) {
        this.setViwedStories(viwedStories);
        return this;
    }

    public AppUser addViwedStory(ViwedStory viwedStory) {
        this.viwedStories.add(viwedStory);
        viwedStory.getAppUsers().add(this);
        return this;
    }

    public AppUser removeViwedStory(ViwedStory viwedStory) {
        this.viwedStories.remove(viwedStory);
        viwedStory.getAppUsers().remove(this);
        return this;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        if (this.driver != null) {
            this.driver.setAppUser(null);
        }
        if (driver != null) {
            driver.setAppUser(this);
        }
        this.driver = driver;
    }

    public AppUser driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public Set<AppUser> getFriendOfs() {
        return this.friendOfs;
    }

    public void setFriendOfs(Set<AppUser> appUsers) {
        if (this.friendOfs != null) {
            this.friendOfs.forEach(i -> i.removeFriends(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addFriends(this));
        }
        this.friendOfs = appUsers;
    }

    public AppUser friendOfs(Set<AppUser> appUsers) {
        this.setFriendOfs(appUsers);
        return this;
    }

    public AppUser addFriendOfs(AppUser appUser) {
        this.friendOfs.add(appUser);
        appUser.getFriends().add(this);
        return this;
    }

    public AppUser removeFriendOfs(AppUser appUser) {
        this.friendOfs.remove(appUser);
        appUser.getFriends().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", login='" + getLogin() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", activated='" + getActivated() + "'" +
            ", langKey='" + getLangKey() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            ", coverPhoto='" + getCoverPhoto() + "'" +
            ", userType='" + getUserType() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", isHiring='" + getIsHiring() + "'" +
            ", enableMessages='" + getEnableMessages() + "'" +
            ", enableNotifications='" + getEnableNotifications() + "'" +
            ", enableOffersNotifications='" + getEnableOffersNotifications() + "'" +
            ", isDark='" + getIsDark() + "'" +
            "}";
    }
}
