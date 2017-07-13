package application.service.impls;

import application.service.interfaces.RoleService;
import application.service.interfaces.UserService;
import application.model.User;
import application.model.UserRole;
import application.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            logger.info("User with id = " + id + " does not exist in the database");
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public User findByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            logger.info("User with name = " + name + " does not exist in the database");
        }
        return user;
    }

    @Transactional
    @Override
    public User add(User user) throws IllegalArgumentException {
        if (user.getId() != null) {
            logger.info("The user with login: " + user.getName() + "  already exists in the database");
            throw new IllegalArgumentException("Логин " + user.getId() + "  уже занят.");
        }
        validate(user);
        //Эта проверяется условие: UNIQUE, которое установлено на поле "name" в таблице "users" в БД
        if (userExist(user.getName())) {
            logger.info("The user with login: " + user.getName() + "  already exists in the database");
            throw new IllegalArgumentException("Логин " + user.getName() + "  уже занят.");
        }
        return userRepository.saveAndFlush(user);
    }
    public User create(String name, String password) throws IllegalArgumentException {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole(roleService.findByName(UserRole.ROLE_USER.toString()));
        return user;
    }

    @Transactional
    @Override
    public User update(User user) throws IllegalArgumentException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("UserID could not be null!");
        }
        validate(user);
        return userRepository.saveAndFlush(user);
    }

    private void validate(User user) throws IllegalArgumentException {
        if (user.getName() == null) {
            logger.info("UserName could not be null!");
            throw new IllegalArgumentException("Логин не может быть пустым!");
        }

        if (user.getName().isEmpty()) {
            logger.info("UserName could not be empty!");
            throw new IllegalArgumentException("Логин не может быть пустым!");
        }

        if (user.getPassword() == null) {
            logger.info("Password could not be null!");
            throw new IllegalArgumentException("Пароль не может быть пустым!");
        }

        if (user.getPassword().isEmpty()) {
            logger.info("Password could not be empty!");
            throw new IllegalArgumentException("Пароль не может быть пустым!");
        }
    }

    private boolean userExist(String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        if (userRepository.findOne(id) == null) {
            logger.info("User with id = " + id + " does not exist in the database");
        } else userRepository.delete(id);
    }
}