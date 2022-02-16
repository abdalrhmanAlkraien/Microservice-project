package asc.foods.store.service;

import asc.foods.store.domain.UserAddress;
import asc.foods.store.repository.UserAddressRepository;
import asc.foods.store.service.dto.UserAddressDTO;
import asc.foods.store.service.mapper.UserAddressMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAddress}.
 */
@Service
@Transactional
public class UserAddressService {

    private final Logger log = LoggerFactory.getLogger(UserAddressService.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressService(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * Save a userAddress.
     *
     * @param userAddressDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAddressDTO save(UserAddressDTO userAddressDTO) {
        log.debug("Request to save UserAddress : {}", userAddressDTO);
        UserAddress userAddress = userAddressMapper.toEntity(userAddressDTO);
        userAddress = userAddressRepository.save(userAddress);
        return userAddressMapper.toDto(userAddress);
    }

    /**
     * Partially update a userAddress.
     *
     * @param userAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserAddressDTO> partialUpdate(UserAddressDTO userAddressDTO) {
        log.debug("Request to partially update UserAddress : {}", userAddressDTO);

        return userAddressRepository
            .findById(userAddressDTO.getId())
            .map(existingUserAddress -> {
                userAddressMapper.partialUpdate(existingUserAddress, userAddressDTO);

                return existingUserAddress;
            })
            .map(userAddressRepository::save)
            .map(userAddressMapper::toDto);
    }

    /**
     * Get all the userAddresses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> findAll() {
        log.debug("Request to get all UserAddresses");
        return userAddressRepository.findAll().stream().map(userAddressMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserAddressDTO> findOne(Long id) {
        log.debug("Request to get UserAddress : {}", id);
        return userAddressRepository.findById(id).map(userAddressMapper::toDto);
    }

    /**
     * Delete the userAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserAddress : {}", id);
        userAddressRepository.deleteById(id);
    }
}
