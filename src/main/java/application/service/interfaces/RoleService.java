package application.service.interfaces;

import application.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Role findById(Integer id);

    Role findByName(String name);

    Role add(Role role);

    Role update(Role role);

    void delete(Integer id);
}
