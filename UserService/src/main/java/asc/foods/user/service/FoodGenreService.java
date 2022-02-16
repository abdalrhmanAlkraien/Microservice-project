package asc.foods.user.service;

import asc.foods.user.domain.FoodGenre;
import asc.foods.user.repository.FoodGenreRepository;
import asc.foods.user.service.dto.FoodGenreDTO;
import asc.foods.user.service.mapper.FoodGenreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodGenre}.
 */
@Service
@Transactional
public class FoodGenreService {

    private final Logger log = LoggerFactory.getLogger(FoodGenreService.class);

    private final FoodGenreRepository foodGenreRepository;

    private final FoodGenreMapper foodGenreMapper;

    public FoodGenreService(FoodGenreRepository foodGenreRepository, FoodGenreMapper foodGenreMapper) {
        this.foodGenreRepository = foodGenreRepository;
        this.foodGenreMapper = foodGenreMapper;
    }

    /**
     * Save a foodGenre.
     *
     * @param foodGenreDTO the entity to save.
     * @return the persisted entity.
     */
    public FoodGenreDTO save(FoodGenreDTO foodGenreDTO) {
        log.debug("Request to save FoodGenre : {}", foodGenreDTO);
        FoodGenre foodGenre = foodGenreMapper.toEntity(foodGenreDTO);
        foodGenre = foodGenreRepository.save(foodGenre);
        return foodGenreMapper.toDto(foodGenre);
    }

    /**
     * Partially update a foodGenre.
     *
     * @param foodGenreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FoodGenreDTO> partialUpdate(FoodGenreDTO foodGenreDTO) {
        log.debug("Request to partially update FoodGenre : {}", foodGenreDTO);

        return foodGenreRepository
            .findById(foodGenreDTO.getId())
            .map(existingFoodGenre -> {
                foodGenreMapper.partialUpdate(existingFoodGenre, foodGenreDTO);

                return existingFoodGenre;
            })
            .map(foodGenreRepository::save)
            .map(foodGenreMapper::toDto);
    }

    /**
     * Get all the foodGenres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FoodGenreDTO> findAll() {
        log.debug("Request to get all FoodGenres");
        return foodGenreRepository.findAll().stream().map(foodGenreMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one foodGenre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FoodGenreDTO> findOne(Long id) {
        log.debug("Request to get FoodGenre : {}", id);
        return foodGenreRepository.findById(id).map(foodGenreMapper::toDto);
    }

    /**
     * Delete the foodGenre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FoodGenre : {}", id);
        foodGenreRepository.deleteById(id);
    }
}
