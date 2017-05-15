package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.Authority;
import com.smbsoft.uniconnect.domain.ModulePage;
import com.smbsoft.uniconnect.security.AuthoritiesConstants;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.ModulePageService;
import com.smbsoft.uniconnect.web.rest.util.HeaderUtil;
import com.smbsoft.uniconnect.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ModulePage.
 */
@RestController
@RequestMapping("/api")
public class ModulePageResource {

    private final Logger log = LoggerFactory.getLogger(ModulePageResource.class);

    private static final String ENTITY_NAME = "modulePage";

    private final ModulePageService modulePageService;

    public ModulePageResource(ModulePageService modulePageService) {
        this.modulePageService = modulePageService;
    }

    /**
     * POST  /module-pages : Create a new modulePage.
     *
     * @param modulePage the modulePage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modulePage, or with status 400 (Bad Request) if the modulePage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/module-pages")
    @Timed
    public ResponseEntity<ModulePage> createModulePage(@Valid @RequestBody ModulePage modulePage) throws URISyntaxException {
        log.debug("REST request to save ModulePage : {}", modulePage);
        if (modulePage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new modulePage cannot already have an ID")).body(null);
        }
        ModulePage result = modulePageService.save(modulePage);
        return ResponseEntity.created(new URI("/api/module-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /module-pages : Updates an existing modulePage.
     *
     * @param modulePage the modulePage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modulePage,
     * or with status 400 (Bad Request) if the modulePage is not valid,
     * or with status 500 (Internal Server Error) if the modulePage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/module-pages")
    @Timed
    public ResponseEntity<ModulePage> updateModulePage(@Valid @RequestBody ModulePage modulePage) throws URISyntaxException {
        log.debug("REST request to update ModulePage : {}", modulePage);
        if (modulePage.getId() == null) {
            return createModulePage(modulePage);
        }
        ModulePage result = modulePageService.save(modulePage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modulePage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /module-pages : get all the modulePages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of modulePages in body
     */
    @GetMapping("/module-pages")
    @Timed
    public ResponseEntity<List<ModulePage>> getAllModulePages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ModulePages");
        Page<ModulePage> page = modulePageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/module-pages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /module-pages/:id : get the "id" modulePage.
     *
     * @param id the id of the modulePage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modulePage, or with status 404 (Not Found)
     */
    @GetMapping("/module-pages/{id}")
    @Timed
    public ResponseEntity<ModulePage> getModulePage(@PathVariable String id) {
        log.debug("REST request to get ModulePage : {}", id);
        ModulePage modulePage = modulePageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modulePage));
    }

    /**
     * DELETE  /module-pages/:id : delete the "id" modulePage.
     *
     * @param id the id of the modulePage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/module-pages/{id}")
    @Timed
    public ResponseEntity<Void> deleteModulePage(@PathVariable String id) {
        log.debug("REST request to delete ModulePage : {}", id);

        // Only admin can do this, check privileges
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            return ResponseEntity.badRequest().build();
        }

        modulePageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
