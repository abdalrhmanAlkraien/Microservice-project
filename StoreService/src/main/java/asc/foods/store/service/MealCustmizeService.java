package asc.foods.store.service;

import asc.foods.store.domain.MealCustmize;
import asc.foods.store.repository.MealCustmizeRepository;
import asc.foods.store.service.dto.MealCustmizeDTO;
import asc.foods.store.service.mapper.MealCustmizeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MealCustmize}.
 */
@Service
@Transactional
public class MealCustmizeService {

    private final Logger log = LoggerFactory.getLogger(MealCustmizeService.class);

    private final MealCustmizeRepository mealCustmizeRepository;

    private final MealCustmizeMapper mealCustmizeMapper;

    public MealCustmizeService(MealCustmizeRepository mealCustmizeRepository, MealCustmizeMapper mealCustmizeMapper) {
        this.mealCustmizeRepository = mealCustmizeRepository;
        this.mealCustmizeMapper = mealCustmizeMapper;
    }

    /**
     * Save a mealCustmize.
     *
     * @param mealCustmizeDTO the entity to save.
     * @return the persisted entity.
     */
    public MealCustmizeDTO save(MealCustmizeDTO mealCustmizeDTO) {
        log.debug("Request to save MealCustmize : {}", mealCustmizeDTO);
        MealCustmize mealCustmize = mealCustmizeMapper.toEntity(mealCustmizeDTO);
        mealCustmize = mealCustmizeRepository.save(mealCustmize);
        return mealCustmizeMapper.toDto(mealCustmize);
    }

    /**
     * Partially update a mealCustmize.
     *
     * @param mealCustmizeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MealCustmizeDTO> partialUpdate(MealCustmizeDTO mealCustmizeDTO) {
        log.debug("Request to partially update MealCustmize : {}", mealCustmizeDTO);

        return mealCustmizeRepository
            .findById(mealCustmizeDTO.getId())
            .map(existingMealCustmize -> {
                mealCustmizeMapper.partialUpdate(existingMealCustmize, mealCustmizeDTO);

                return existingMealCustmize;
            })
            .map(mealCustmizeRepository::save)
            .map(mealCustmizeMapper::toDto);
    }

    /**
     * Get all the mealCustmizes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MealCustmizeDTO> findAll() {
        log.debug("Request to get all MealCustmizes");
        return mealCustmizeRepository.findAll().stream().map(mealCustmizeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mealCustmize by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MealCustmizeDTO> findOne(Long id) {
        log.debug("Request to get MealCustmize : {}", id);
        return mealCustmizeRepository.findById(id).map(mealCustmizeMapper::toDto);
    }

    /**
     * Delete the mealCustmize by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MealCustmize : {}", id);
        mealCustmizeRepository.deleteById(id);
    }
}
