package com.smbsoft.uniconnect.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.smbsoft.uniconnect.domain.Post;
import com.smbsoft.uniconnect.security.AuthoritiesConstants;
import com.smbsoft.uniconnect.security.SecurityUtils;
import com.smbsoft.uniconnect.service.PostService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Post.
 */
@RestController
@RequestMapping("/api")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    private final PostService postService;

    public PostResource(PostService postService) {
        this.postService = postService;
    }

    /**
     * POST  /posts : Create a new post.
     *
     * @param post the post to create, id of the module
     * @return the ResponseEntity with status 201 (Created) and with body the new post, or with status 400 (Bad Request) if the post has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/posts/module/{id}")
    @Timed
    public ResponseEntity<Post> createPostModule(@Valid @RequestBody Post post, @PathVariable String id) throws URISyntaxException {
        log.debug("REST request to save Post to module : {}", post);

        if (post.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new post cannot already have an ID")).body(null);
        }
        Post result = postService.saveModule(post, id);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /posts/module/{id}: Create a new post.
     *
     * @param post the post to create
     * @return the ResponseEntity with status 201 (Created) and with body the new post, or with status 400 (Bad Request) if the post has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/posts")
    @Timed
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to save Post : {}", post);
        if (post.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new post cannot already have an ID")).body(null);
        }

        // Fill up with empty arrays to eliminate null
        post.setComments(new ArrayList<>());
        post.setTags(new ArrayList<>());
        post.setVoteUsers(new ArrayList<>());

        Post result = postService.save(post);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    /**
     * PUT  /posts : Updates an existing post.
     *
     * @param post the post to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated post,
     * or with status 400 (Bad Request) if the post is not valid,
     * or with status 500 (Internal Server Error) if the post couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/posts")
    @Timed
    public ResponseEntity<Post> updatePost(@Valid @RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to update Post : {}", post);
        if (post.getId() == null) {
            return createPost(post);
        }
        Post result = postService.save(post);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, post.getId().toString()))
            .body(result);
    }

    /**
     * GET  /posts : get all the posts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of posts in body
     */
    @GetMapping("/posts")
    @Timed
    public ResponseEntity<List<Post>> getAllPosts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<Post> page = postService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /posts/:id : get the "id" post.
     *
     * @param id the id of the post to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the post, or with status 404 (Not Found)
     */
    @GetMapping("/posts/{id}")
    @Timed
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        log.debug("REST request to get Post : {}", id);
        Post post = postService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(post));
    }

    /**
     * DELETE  /posts/:id : delete the "id" post.
     *
     * @param id the id of the post to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/posts/{id}")
    @Timed
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        log.debug("REST request to delete Post : {}", id);

        // Should be either a admin or the creator
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.getCurrentUserLogin().equals(postService.findOne(id).getOwnerLogin())){
            postService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        }else{
            // Otherwise cannot delete, so bad request
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE  /posts/:id : delete the "id" post.
     *
     * @param id the id of the post to delete
     * @param moduleId the id of the module page
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/posts/{id}/modules/{moduleId}")
    @Timed
    public ResponseEntity<Void> deletePostOfModulePage(@PathVariable String id, @PathVariable String moduleId) {
        log.debug("REST request to delete Post : {} of Module : {}", id, moduleId);

        // Should be either a admin or the creator
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.getCurrentUserLogin().equals(postService.findOne(id).getOwnerLogin())){
            postService.deleteOfModulePage(id, moduleId);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
        }else{
            // Otherwise cannot delete, so bad request
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET  /posts/module/ :id : get the posts for module having "id"
     *
     * @param module the id of the module of which posts needs to be retrieved
     * @return the ResponseEntity with status 200 (OK) and with body the post, or with status 404 (Not Found)
     */
    @GetMapping("/posts/module/{module}")
    @Timed
    public ResponseEntity<List<Post>> getPostForModule(@PathVariable String module) {
        log.debug("REST request to get Post of module : {}", module);

        List<Post> posts = postService.findByModule(module);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(posts));
    }

    /**
     * GET  /posts/:postId/:userLogin : up vote the post having postId by userLogin
     *
     * @param postId the id of the post to upvote
     * @param userLogin the login of the user who upvoted
     * @return the ResponseEntity with status 200 (OK) and with body the post, or with status 404 (Not Found)
     */
    @GetMapping("/posts/{postId}/{userLogin}")
    @Timed
    public ResponseEntity<Post> upvotePost(@PathVariable String postId, @PathVariable String userLogin) {
        log.debug("REST request to upvote Post : {}", postId);
        Post post = postService.upvotePost(postId, userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(post));
    }

}
