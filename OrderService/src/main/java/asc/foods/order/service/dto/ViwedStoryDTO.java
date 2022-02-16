package asc.foods.order.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link asc.foods.order.domain.ViwedStory} entity.
 */
public class ViwedStoryDTO implements Serializable {

    private Long id;

    private Instant viewdTine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getViewdTine() {
        return viewdTine;
    }

    public void setViewdTine(Instant viewdTine) {
        this.viewdTine = viewdTine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ViwedStoryDTO)) {
            return false;
        }

        ViwedStoryDTO viwedStoryDTO = (ViwedStoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, viwedStoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ViwedStoryDTO{" +
            "id=" + getId() +
            ", viewdTine='" + getViewdTine() + "'" +
            "}";
    }
}
