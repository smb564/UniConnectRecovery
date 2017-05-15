package com.smbsoft.uniconnect.service;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.smbsoft.uniconnect.domain.ModulePage;
import com.smbsoft.uniconnect.domain.Post;
import com.smbsoft.uniconnect.repository.PostRepository;
import com.smbsoft.uniconnect.security.SecurityUtils;
import javafx.scene.control.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Security;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Post.
 */
@Service
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    private CommentService commentService;

    @Autowired
    private ModulePageService modulePageService;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    public void setCommentService(CommentService commentService){
        this.commentService = commentService;
    }



    /**
     * Save a post.
     *
     * @param post the entity to save
     * @return the persisted entity
     */
    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);

        Post result = postRepository.save(post);

        return result;
    }

    /**
     * Save a post.
     *
     * @param post the entity to save
     * @return the persisted entity
     */
    public Post saveModule(Post post, String moduleId) {
        log.debug("Request to save Post : {}", post);

        // post owner should be the current logged in user
        Post result = postRepository.save(post
            .ownerLogin(SecurityUtils.getCurrentUserLogin())
            .date(LocalDate.now())
            .votes(0)
        );

        // And the new post should be added to the module page
        ModulePage modulePage = modulePageService.findOne(moduleId);

        List<String> tempList = modulePage.getPosts();

        if (tempList == null){
            modulePage.setPosts(new ArrayList<String>());
            modulePage.getPosts().add(result.getId());
        }
        else{
            modulePage.getPosts().add(result.getId());
        }

        modulePageService.save(modulePage);

        return result;
    }

    /**
     *  Get all the posts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Post> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        Page<Post> result = postRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one post by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Post findOne(String id) {
        log.debug("Request to get Post : {}", id);
        Post post = postRepository.findOne(id);
        return post;
    }

    /**
     *  Delete the  post by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Post : {}", id);

        // delete all the comments related to this post
        Post post = postRepository.findOne(id);
        Optional<List<String>> commentsOptional = Optional.ofNullable(post.getComments());

        // Delete all if present
        if(commentsOptional.isPresent()){
            for(String comment : commentsOptional.get()){
                commentService.delete(comment);
            }
        }

        // Now delete the post
        postRepository.delete(id);
    }

    public void deleteOfModulePage(String id, String moduleId){
        // Remove from the post list
        ModulePage modulePage = modulePageService.findOne(moduleId);

        Iterator<String> iter = modulePage.getPosts().iterator();

        while(iter.hasNext()){
            if (iter.next().equals(id)){
                iter.remove();
                break;
            }
        }

        modulePageService.save(modulePage);

        // Now delete the post
        delete(id);
    }

    public List<Post> findByModule(String moduleId) {
        ModulePage modulePage = modulePageService.findOne(moduleId);
        List<Post> posts = new ArrayList<>();

        if (modulePage.getPosts() == null)
            return new ArrayList<>();

        for(String postId : modulePage.getPosts()){
            posts.add(postRepository.findOne(postId));
        }

        return posts;
    }

    public Post upvotePost(String postId, String userLogin){
        log.debug("Enetered into upvote method");
        Post post = postRepository.findOne(postId);

        Optional<List<String>> voteUserOptional = Optional.ofNullable(post.getVoteUsers());

        if (!voteUserOptional.isPresent()){
            log.debug("Vote User List not exist, so first time up vote");
            post.setVoteUsers(new ArrayList<>());
            post.getVoteUsers().add(userLogin);
            post.incrementVotes();
            return save(post);
        }

        Iterator<String> iter = post.getVoteUsers().iterator();

        while(iter.hasNext()){
            if (userLogin.equals(iter.next())){
                log.debug("Already voted user, remove the vote");
                // Already voted user
                // decrement votes and remove userLogin from the vote list
                post.decrementVotes();
                iter.remove();
                return save(post);
            }
        }

        // If nothing returned, the user should be incrementing votes
        log.debug("New vote from a new user, upvote");
        post.incrementVotes();
        post.getVoteUsers().add(userLogin);

        return save(post);
    }
}
