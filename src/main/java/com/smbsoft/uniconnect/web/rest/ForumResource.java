package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.Forum;
import com.smbsoft.uniconnect.domain.Post;
import com.smbsoft.uniconnect.security.AuthoritiesConstants;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.ForumService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Forum.
 */
@RestController
@RequestMapping("/api")
public class ForumResource {

    private final Logger log = LoggerFactory.getLogger(ForumResource.class);

    private static final String ENTITY_NAME = "forum";

    private final ForumService forumService;

    public ForumResource(ForumService forumService) {
        this.forumService = forumService;
    }

    /**
     * POST  /forums : Create a new forum.
     *
     * @param forum the forum to create
     * @return the ResponseEntity with status 201 (Created) and with body the new forum, or with status 400 (Bad Request) if the forum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forums")
    @Timed
    public ResponseEntity<Forum> createForum(@RequestBody Forum forum) throws URISyntaxException {
        log.debug("REST request to save Forum : {}", forum);
        if (forum.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new forum cannot already have an ID")).body(null);
        }
        Forum result = forumService.save(forum);
        return ResponseEntity.created(new URI("/api/forums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /forums : Updates an existing forum.
     *
     * @param forum the forum to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated forum,
     * or with status 400 (Bad Request) if the forum is not valid,
     * or with status 500 (Internal Server Error) if the forum couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/forums")
    @Timed
    public ResponseEntity<Forum> updateForum(@RequestBody Forum forum) throws URISyntaxException {
        log.debug("REST request to update Forum : {}", forum);
        if (forum.getId() == null) {
            return createForum(forum);
        }
        Forum result = forumService.save(forum);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, forum.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forums : get all the forums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of forums in body
     */
    @GetMapping("/forums")
    @Timed
    public ResponseEntity<List<Forum>> getAllForums(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Forums");
        Page<Forum> page = forumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/forums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /forums/:id : get the "id" forum.
     *
     * @param id the id of the forum to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the forum, or with status 404 (Not Found)
     */
    @GetMapping("/forums/{id}")
    @Timed
    public ResponseEntity<Forum> getForum(@PathVariable String id) {
        log.debug("REST request to get Forum : {}", id);
        Forum forum = forumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(forum));
    }

    /**
     * DELETE  /forums/:id : delete the "id" forum.
     *
     * @param id the id of the forum to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/forums/{id}")
    @Timed
    public ResponseEntity<Void> deleteForum(@PathVariable String id) {
        log.debug("REST request to delete Forum : {}", id);
        forumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /forum/posts : Create a new post in the forum.
     *
     * @param id of the forum
     * @param post the post to create, id of the module
     * @return the ResponseEntity with status 201 (Created) and with body the new post, or with status 400 (Bad Request) if the post has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forums/{id}/posts")
    @Timed
    public ResponseEntity<Post> addPost(@Valid @RequestBody Post post, @PathVariable String id) throws URISyntaxException {
        log.debug("REST request to save Post to Forum : {}", post);

        if (post.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new post cannot already have an ID")).body(null);
        }
        post = post.ownerLogin(SecurityUtils.getCurrentUserLogin()).date(LocalDate.now()).votes(0);
        Post result = forumService.addPostToForum(post, id);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forums : get all the posts for forum
     *
     * @param id of the forum
     * @return the ResponseEntity with status 200 (OK) and the list of forums in body
     */
    @GetMapping("/forums/{id}/posts")
    @Timed
    public ResponseEntity<List<Post>> getPostsForForum(@PathVariable String id) {
        log.debug("REST request to get a page of Forums");

        List<Post> posts = forumService.getPosts(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(posts));
    }

    /**
     * DELETE  /posts/:id : delete the "id" post.
     *
     * @param id the id of the post to delete
     * @param forumId the id of the forum
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/posts/{id}/forum/{forumId}")
    @Timed
    public ResponseEntity<Void> deletePostOfForum(@PathVariable String id, @PathVariable String forumId) {
        log.debug("REST request to delete Post : {} of Forum : {}", id, forumId);

        // Should be either a admin or the creator
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.getCurrentUserLogin().equals(forumService.postIdentity(id))){
            forumService.deletePost(id, forumId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        }else{
            // Otherwise cannot delete, so bad request
            return ResponseEntity.badRequest().build();
        }
    }

}
