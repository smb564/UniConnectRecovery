package com.smbsoft.uniconnect.service;

import com.smbsoft.uniconnect.domain.Forum;
import com.smbsoft.uniconnect.domain.Post;
import com.smbsoft.uniconnect.repository.ForumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Forum.
 */
@Service
public class ForumService {

    private final Logger log = LoggerFactory.getLogger(ForumService.class);

    private final ForumRepository forumRepository;

    private final PostService postService;

    public ForumService(ForumRepository forumRepository, PostService postService) {
        this.forumRepository = forumRepository;
        this.postService = postService;
    }

    /**
     * Save a forum.
     *
     * @param forum the entity to save
     * @return the persisted entity
     */
    public Forum save(Forum forum) {
        log.debug("Request to save Forum : {}", forum);
        Forum result = forumRepository.save(forum);
        return result;
    }

    /**
     *  Get all the forums.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Forum> findAll(Pageable pageable) {
        log.debug("Request to get all Forums");
        Page<Forum> result = forumRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one forum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Forum findOne(String id) {
        log.debug("Request to get Forum : {}", id);
        Forum forum = forumRepository.findOne(id);
        return forum;
    }

    /**
     *  Delete the  forum by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Forum : {}", id);

        // Delete all the posts related to the forum as well
        Forum forum = forumRepository.findOne(id);
        Optional<List<String>> postsOptional = Optional.ofNullable(forum.getPosts());

        if(postsOptional.isPresent()){
            for(String post : postsOptional.get()){
                postService.delete(post);
            }
        }

        forumRepository.delete(id);
    }

    public Post addPostToForum(Post post, String forumId){
        Post result = postService.save(post);

        // Add the psot id to the Forum posts list
        Forum forum = forumRepository.findOne(forumId);

        Optional<List<String>> postsOptional = Optional.ofNullable(forum.getPosts());

        if(!postsOptional.isPresent()){
            // create a new list
            forum.setPosts(new ArrayList<>());
        }

        // Now add the new post id
        forum.getPosts().add(result.getId());

        //save the forum
        save(forum);

        return post;

    }

    public List<Post> getPosts(String forumId){
        Forum forum = forumRepository.findOne(forumId);

        Optional<List<String>> postsOptional = Optional.ofNullable(forum.getPosts());

        if(!postsOptional.isPresent()){
            // create a new list
            return new ArrayList<>();
        }

        // else return the posts
        List<Post> postList = new ArrayList<>();

        for(String post: postsOptional.get()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    postList.add(postService.findOne(post));
                }
            }).start();
        }

        // Wait till all posts are taken
        while(postList.size() != postsOptional.get().size()){
            // wait
        }

        // now return the posts
        return postList;

    }

    public void deletePost(String postId, String forumId){
        Forum forum = forumRepository.findOne(forumId);

        Iterator<String> iter = forum.getPosts().iterator();

        while(iter.hasNext()){
            if (iter.next().equals(postId)){
                iter.remove();
                break;
            }
        }

        save(forum);

        postService.delete(postId);
    }

    public String postIdentity(String postId){
        return postService.findOne(postId).getOwnerLogin();
    }
}
