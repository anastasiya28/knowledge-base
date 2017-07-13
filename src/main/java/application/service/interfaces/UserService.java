package application.service.interfaces;

import application.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Integer id);

    User findByName(String name);

    User create(String name, String password);

    User add(User user);

    User update(User user);

    void delete(Integer id);
}
