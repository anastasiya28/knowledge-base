package application.security;

import application.service.interfaces.RoleService;
import application.service.interfaces.UserService;
import application.model.Role;
import application.model.User;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AccessJwtToken {
    private static final Logger logger = Logger.getLogger(AccessJwtToken.class);

    private String token;

    @Autowired
    JwtSettings jwtSettings;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String createAccessJwtToken(User user) throws IllegalArgumentException {
        User userFromDB = userService.findByName(user.getName());

        if (userService.findByName(user.getName()) == null) {
            throw new IllegalArgumentException("A user with this name does not exist in the database");
        }

        if (userFromDB.getName().equals(user.getName()) & userFromDB.getPassword().equals(user.getPassword())) {
            Claims claims = Jwts.claims().setSubject(userFromDB.getName());
            claims.put("userId", userFromDB.getId());
            claims.put("userRoleName", userFromDB.getRole().getName());

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

            return token = Jwts.builder()
                    .setClaims(claims)
                    .signWith(signatureAlgorithm, jwtSettings.getSigningKey())
                    .compact();
//            return token;
        } else {
            throw new IllegalArgumentException("Incorrect data entered: name or password");
        }
    }

    public User parseClaims(String token) {
        User user = new User();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSettings.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            user.setName(claims.getSubject());
            user.setId((Integer) claims.get("userId"));

            Role role = roleService.findByName((String) claims.get("userRoleName"));
            if(role != null) {
                user.setRole(role);
            }
            return user;

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            logger.trace("Invalid JWT Token", e);
            throw e;
        }
    }
}
