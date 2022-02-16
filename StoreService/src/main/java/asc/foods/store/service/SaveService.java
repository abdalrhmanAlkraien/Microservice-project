package asc.foods.store.service;

import asc.foods.store.domain.Save;
import asc.foods.store.repository.SaveRepository;
import asc.foods.store.service.dto.SaveDTO;
import asc.foods.store.service.mapper.SaveMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Save}.
 */
@Service
@Transactional
public class SaveService {

    private final Logger log = LoggerFactory.getLogger(SaveService.class);

    private final SaveRepository saveRepository;

    private final SaveMapper saveMapper;

    public SaveService(SaveRepository saveRepository, SaveMapper saveMapper) {
        this.saveRepository = saveRepository;
        this.saveMapper = saveMapper;
    }

    /**
     * Save a save.
     *
     * @param saveDTO the entity to save.
     * @return the persisted entity.
     */
    public SaveDTO save(SaveDTO saveDTO) {
        log.debug("Request to save Save : {}", saveDTO);
        Save save = saveMapper.toEntity(saveDTO);
        save = saveRepository.save(save);
        return saveMapper.toDto(save);
    }

    /**
     * Partially update a save.
     *
     * @param saveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaveDTO> partialUpdate(SaveDTO saveDTO) {
        log.debug("Request to partially update Save : {}", saveDTO);

        return saveRepository
            .findById(saveDTO.getId())
            .map(existingSave -> {
                saveMapper.partialUpdate(existingSave, saveDTO);

                return existingSave;
            })
            .map(saveRepository::save)
            .map(saveMapper::toDto);
    }

    /**
     * Get all the saves.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SaveDTO> findAll() {
        log.debug("Request to get all Saves");
        return saveRepository.findAll().stream().map(saveMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one save by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaveDTO> findOne(Long id) {
        log.debug("Request to get Save : {}", id);
        return saveRepository.findById(id).map(saveMapper::toDto);
    }

    /**
     * Delete the save by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Save : {}", id);
        saveRepository.deleteById(id);
    }
}
