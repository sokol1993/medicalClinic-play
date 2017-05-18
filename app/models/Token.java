package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by sokol on 2017-03-10.
 */
@Entity
public class Token extends Model {

    @Id
    public Long id;
    public String token;

    @OneToOne(mappedBy = "token")
    public User user;

    public static Finder<Long, Token> find = new Finder<Long, Token>(Token.class);
}
