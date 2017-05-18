package models;

import java.util.*;
import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by sokol on 2017-03-08.
 */
@Entity
public class Patient extends Model {

    @Id
    public Long id;

    @OneToOne(mappedBy = "patient")
    public User user;

    public String firstName;
    public String lastName;

    public String email;
    @Temporal(TemporalType.DATE)
    public Date dateOfBirth;
    public String pesel;

    public String streetName;
    public String streetNumber;
    public String homeNumber;
    public String postcode;
    public String city;

    public static Finder<Long, Patient> find = new Finder<Long, Patient>(Patient.class);
}
