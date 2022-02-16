package asc.foods.store.service.dto;

import asc.foods.store.domain.enumeration.Gender;
import asc.foods.store.domain.enumeration.UserStatus;
import asc.foods.store.domain.enumeration.UserType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link asc.foods.store.domain.AppUser} entity.
 */
public class AppUserDTO implements Serializable {

    private String id;

    private String mobileNumber;

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean activated;

    private String langKey;

    private String imageUrl;

    private UserStatus userStatus;

    private String coverPhoto;

    private UserType userType;

    private String dob;

    private Gender gender;

    private Boolean isHiring;

    private Boolean enableMessages;

    private Boolean enableNotifications;

    private Boolean enableOffersNotifications;

    private Boolean isDark;

    private Set<AppUserDTO> friends = new HashSet<>();

    private Set<ViwedStoryDTO> viwedStories = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Boolean getIsHiring() {
        return isHiring;
    }

    public void setIsHiring(Boolean isHiring) {
        this.isHiring = isHiring;
    }

    public Boolean getEnableMessages() {
        return enableMessages;
    }

    public void setEnableMessages(Boolean enableMessages) {
        this.enableMessages = enableMessages;
    }

    public Boolean getEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(Boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public Boolean getEnableOffersNotifications() {
        return enableOffersNotifications;
    }

    public void setEnableOffersNotifications(Boolean enableOffersNotifications) {
        this.enableOffersNotifications = enableOffersNotifications;
    }

    public Boolean getIsDark() {
        return isDark;
    }

    public void setIsDark(Boolean isDark) {
        this.isDark = isDark;
    }

    public Set<AppUserDTO> getFriends() {
        return friends;
    }

    public void setFriends(Set<AppUserDTO> friends) {
        this.friends = friends;
    }

    public Set<ViwedStoryDTO> getViwedStories() {
        return viwedStories;
    }

    public void setViwedStories(Set<ViwedStoryDTO> viwedStories) {
        this.viwedStories = viwedStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id='" + getId() + "'" +
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
            ", friends=" + getFriends() +
            ", viwedStories=" + getViwedStories() +
            "}";
    }
}
