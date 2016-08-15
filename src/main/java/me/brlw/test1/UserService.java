package me.brlw.test1;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by vl on 06.08.16.
 */
public interface UserService {
    Page<User> findAll(Integer pageNum);
    User findById(Long id);
    Page<User> findAllByName(Integer pageNum, String search_str);
    User save(User user);
    void delete(User user);
}
