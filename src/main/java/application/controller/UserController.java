package application.controller;

import application.service.interfaces.UserService;
import application.model.User;
import application.security.AccessJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController

public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AccessJwtToken accessJwtToken;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String login(@RequestParam("login") String userName, @RequestParam("password") String password,
                        HttpServletResponse response) throws
            IllegalArgumentException, IOException, ServletException {

        if (userName.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Логин не заполнен!\"}";
        }

        if (password.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Пароль не заполнен!\"}";
        }

        User user = userService.findByName(userName);

        if (user == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
            return "{\"errorMessage\":\"Неверный логин или пароль.\"}";
        }

        if (user.getName().equals(userName) & user.getPassword().equals(user.md5Hash(password))) {
            String token = accessJwtToken.createAccessJwtToken(user);
            String jsonToken;
            if (token == null) return "[]";
            jsonToken = "{" +
                    "\"token\":\"" + accessJwtToken.getToken() + "\"" +
                    ",\"userrole\":\"" + user.getRole().getName() + "\"" +
                    "}";
            return jsonToken;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401
            return "{\"errorMessage\":\"Неверный логин или пароль.\"}";
        }
    }

    @RequestMapping(value = "/user/new", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public String addNewUser(@RequestParam("login") String userName, @RequestParam("password") String password,
                             HttpServletResponse response) throws
            IllegalArgumentException, IOException, ServletException {
        if (userName.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Логин не заполнен!\"}";
        }

        if (password.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());//400
            return "{\"errorMessage\":\"Пароль не заполнен!\"}";
        }

        User userFromDB;
        User user = userService.create(userName, password);
        try {
            userFromDB = userService.add(user);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());//401
            return "{\"errorMessage\":\"" + e.getMessage() + "\"}";
        }

        if (userFromDB == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }

        if (user.getName().equals(userName) & user.getPassword().equals(user.md5Hash(password))) {
            String token = accessJwtToken.createAccessJwtToken(user);
            String jsonToken;
            if (token == null) return "[]";
            jsonToken = "{" +
                    "\"token\":\"" + accessJwtToken.getToken() + "\"" +
                    ",\"userrole\":\"" + user.getRole().getName() + "\"" +
                    "}";
            return jsonToken;
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //500
            return "{\"errorMessage\":\"Внутренняя ошибка сервера.\"}";
        }
    }

    @RequestMapping(value = "/api/user/list", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public String findUsersAll() {
        List<User> users = userService.findAll();
        String json;
        if (users.size() == 0) return "[]";
        json = "[";
        for (User user : users) {
            String item = "{";
            item += "\"id\":" + user.getId() + ",\"name\":\"" + user.getName() + "\"";
            item += "},";
            json += item;
        }
        json = json.substring(0, json.length() - 1);
        json += "]";
        return json;
    }
}