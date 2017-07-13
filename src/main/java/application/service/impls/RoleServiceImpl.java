package application.service.impls;

import application.service.interfaces.RoleService;
import application.model.Role;
import application.repository.RoleRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(Integer id) {
        Role role = roleRepository.findOne(id);
        if (role == null) {
            logger.info("Role with id = " + id + " does not exist in the database");
        }
        return role;
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByName(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            logger.info("Role with name = " + name + " does not exist in the database");
        }
        return role;
    }

    @Transactional
    @Override
    public Role add(Role role) {
        if (role.getId() != null) {
            throw new IllegalArgumentException("The role with id: " + role.getId() + "  already exists in the database");
        }
        validate(role);
        if (roleExist(role.getName())) {
            throw new IllegalArgumentException("The role with login: " + role.getName() + "  already exists in the database");
        }
        return roleRepository.saveAndFlush(role);
    }

    @Transactional
    @Override
    public Role update(Role role) {
        if (role.getId() == null) {
            throw new IllegalArgumentException("RoleID could not be null!");
        }
        validate(role);
        return roleRepository.saveAndFlush(role);
    }

    private void validate(Role role) throws IllegalArgumentException {
        if (role.getName() == null) {
            throw new IllegalArgumentException("RoleName could not be null!");
        }

        if (role.getName().isEmpty()) {
            throw new IllegalArgumentException("RoleName could not be empty!");
        }
    }

    private boolean roleExist(String name) {
        Role role = roleRepository.findByName(name);
        if (role != null) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        if (roleRepository.findOne(id) == null) {
            logger.info("Role with id = " + id + " does not exist in the database");
        } else roleRepository.delete(id);
    }
}
