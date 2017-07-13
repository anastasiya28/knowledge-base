package application.model;

import org.apache.log4j.Logger;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name = "users_seq")
    @GeneratedValue(generator = "users_seq", strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private static final Logger logger = Logger.getLogger(User.class);

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = md5Hash(password);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //MD5 (Message Digest 5) — 128-битный алгоритм хеширования, разработанный профессором Рональдом Л. Ривестом в 1991 году.
    //Хеш содержит 128 бит (16 байт) поэтому мы в строке 17 указали 16 байтов, в строке 19 было указанно 32 так как обычно
    //хеш 16 байтов представляется как последовательность из 32 шестнадцатеричных цифр.
    public String md5Hash(String password) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.trace(e);
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String hashPassword = bigInt.toString(16);

        while (hashPassword.length() < 32) {
            hashPassword = "0" + hashPassword;
        }
        return hashPassword;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ((name == null) ? 0 : name.hashCode());
        result = 31 * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }
}

