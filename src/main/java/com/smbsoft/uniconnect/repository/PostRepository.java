package com.smbsoft.uniconnect.repository;

import com.smbsoft.uniconnect.domain.Post;

import javafx.scene.control.Pagination;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Spring Data MongoDB repository for the Post entity.
 */
@SuppressWarnings("unused")
public interface PostRepository extends MongoRepository<Post,String> {
}
