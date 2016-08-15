package me.brlw.test1;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Created by vl on 06.08.16.
 */

@Repository
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService
{
    private static final int PAGE_SIZE = 5;

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    @Transactional(readOnly=true)
    @Override
    public Page<User> findAll(Integer pageNum)
    {
        PageRequest req = new PageRequest(pageNum - 1, PAGE_SIZE, Sort.Direction.ASC, "id");
        return userRepository.findAll(req);
    }

    @Transactional(readOnly=true)
    @Override
    public Page<User> findAllByName(Integer pageNum, String search_str)
    {
        PageRequest req = new PageRequest(pageNum - 1, PAGE_SIZE, Sort.Direction.ASC, "id");
        return userRepository.findAllByName(search_str, req);
    }

    @Transactional(readOnly=true)
    @Override
    public User findById(Long id)
    {
        return userRepository.findOne(id);
    }

    @Transactional("transactionManager")
    @Override
    public User save(User user)
    {
        User savedUser = userRepository.save(user);
        LOG.info("User updated with id: " + savedUser.getId());
        return savedUser;
    }

    @Transactional("transactionManager")
    @Override
    public void delete(User user) {
        userRepository.delete(user);
        LOG.info("User deleted with id: " + user.getId());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
