package org.nh.artha.service.impl;

import org.nh.artha.service.McrStagingService;
import org.nh.artha.domain.McrStaging;
import org.nh.artha.repository.McrStagingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link McrStaging}.
 */
@Service
@Transactional
public class McrStagingServiceImpl implements McrStagingService {

    private final Logger log = LoggerFactory.getLogger(McrStagingServiceImpl.class);

    private final McrStagingRepository mcrStagingRepository;

    public McrStagingServiceImpl(McrStagingRepository mcrStagingRepository) {
        this.mcrStagingRepository = mcrStagingRepository;

    }

    /**
     * Save a mcrStaging.
     *
     * @param mcrStaging the entity to save.
     * @return the persisted entity.
     */
    @Override
    public McrStaging save(McrStaging mcrStaging) {
        log.debug("Request to save McrStaging : {}", mcrStaging);
        McrStaging result = mcrStagingRepository.save(mcrStaging);
        return result;
    }

    /**
     * Get all the mcrStagings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<McrStaging> findAll(Pageable pageable) {
        log.debug("Request to get all McrStagings");
        return mcrStagingRepository.findAll(pageable);
    }


    /**
     * Get one mcrStaging by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<McrStaging> findOne(Long id) {
        log.debug("Request to get McrStaging : {}", id);
        return mcrStagingRepository.findById(id);
    }

    /**
     * Delete the mcrStaging by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete McrStaging : {}", id);
        mcrStagingRepository.deleteById(id);
    }



}
