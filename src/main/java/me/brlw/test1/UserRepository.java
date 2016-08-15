package me.brlw.test1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by vl on 07.08.16.
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    @Query("SELECT u FROM User u where LOWER(u.name) LIKE CONCAT('%',LOWER(:search_str),'%')")
    Page<User> findAllByName(@Param("search_str") String search_str, Pageable pageable);
}
