package asc.foods.user.service;

import asc.foods.user.domain.ReadBy;
import asc.foods.user.repository.ReadByRepository;
import asc.foods.user.service.dto.ReadByDTO;
import asc.foods.user.service.mapper.ReadByMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReadBy}.
 */
@Service
@Transactional
public class ReadByService {

    private final Logger log = LoggerFactory.getLogger(ReadByService.class);

    private final ReadByRepository readByRepository;

    private final ReadByMapper readByMapper;

    public ReadByService(ReadByRepository readByRepository, ReadByMapper readByMapper) {
        this.readByRepository = readByRepository;
        this.readByMapper = readByMapper;
    }

    /**
     * Save a readBy.
     *
     * @param readByDTO the entity to save.
     * @return the persisted entity.
     */
    public ReadByDTO save(ReadByDTO readByDTO) {
        log.debug("Request to save ReadBy : {}", readByDTO);
        ReadBy readBy = readByMapper.toEntity(readByDTO);
        readBy = readByRepository.save(readBy);
        return readByMapper.toDto(readBy);
    }

    /**
     * Partially update a readBy.
     *
     * @param readByDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReadByDTO> partialUpdate(ReadByDTO readByDTO) {
        log.debug("Request to partially update ReadBy : {}", readByDTO);

        return readByRepository
            .findById(readByDTO.getId())
            .map(existingReadBy -> {
                readByMapper.partialUpdate(existingReadBy, readByDTO);

                return existingReadBy;
            })
            .map(readByRepository::save)
            .map(readByMapper::toDto);
    }

    /**
     * Get all the readBies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReadByDTO> findAll() {
        log.debug("Request to get all ReadBies");
        return readByRepository.findAll().stream().map(readByMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one readBy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReadByDTO> findOne(Long id) {
        log.debug("Request to get ReadBy : {}", id);
        return readByRepository.findById(id).map(readByMapper::toDto);
    }

    /**
     * Delete the readBy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReadBy : {}", id);
        readByRepository.deleteById(id);
    }
}
