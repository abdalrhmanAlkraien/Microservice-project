package asc.foods.user.service;

import asc.foods.user.domain.PromoCode;
import asc.foods.user.repository.PromoCodeRepository;
import asc.foods.user.service.dto.PromoCodeDTO;
import asc.foods.user.service.mapper.PromoCodeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PromoCode}.
 */
@Service
@Transactional
public class PromoCodeService {

    private final Logger log = LoggerFactory.getLogger(PromoCodeService.class);

    private final PromoCodeRepository promoCodeRepository;

    private final PromoCodeMapper promoCodeMapper;

    public PromoCodeService(PromoCodeRepository promoCodeRepository, PromoCodeMapper promoCodeMapper) {
        this.promoCodeRepository = promoCodeRepository;
        this.promoCodeMapper = promoCodeMapper;
    }

    /**
     * Save a promoCode.
     *
     * @param promoCodeDTO the entity to save.
     * @return the persisted entity.
     */
    public PromoCodeDTO save(PromoCodeDTO promoCodeDTO) {
        log.debug("Request to save PromoCode : {}", promoCodeDTO);
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCode = promoCodeRepository.save(promoCode);
        return promoCodeMapper.toDto(promoCode);
    }

    /**
     * Partially update a promoCode.
     *
     * @param promoCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PromoCodeDTO> partialUpdate(PromoCodeDTO promoCodeDTO) {
        log.debug("Request to partially update PromoCode : {}", promoCodeDTO);

        return promoCodeRepository
            .findById(promoCodeDTO.getId())
            .map(existingPromoCode -> {
                promoCodeMapper.partialUpdate(existingPromoCode, promoCodeDTO);

                return existingPromoCode;
            })
            .map(promoCodeRepository::save)
            .map(promoCodeMapper::toDto);
    }

    /**
     * Get all the promoCodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PromoCodeDTO> findAll() {
        log.debug("Request to get all PromoCodes");
        return promoCodeRepository.findAll().stream().map(promoCodeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one promoCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PromoCodeDTO> findOne(Long id) {
        log.debug("Request to get PromoCode : {}", id);
        return promoCodeRepository.findById(id).map(promoCodeMapper::toDto);
    }

    /**
     * Delete the promoCode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PromoCode : {}", id);
        promoCodeRepository.deleteById(id);
    }
}
