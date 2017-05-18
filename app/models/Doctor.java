package models;

import java.util.*;

import com.avaje.ebean.FetchPath;
import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by sokol on 2017-03-08.
 */
@Entity
public class Doctor extends Model {

    @Id
    public Long id;

    @OneToOne(mappedBy = "doctor")
    public User user;

    public String firstName;
    public String lastName;

    @ManyToOne
    public Specialization specialization;

    public String mondayHours;
    public String tuesdayHours;
    public String wednesdayHours;
    public String thursdayHours;
    public String fridayHours;

    public static Finder<Long, Doctor> find = new Finder<Long, Doctor>(Doctor.class);

}
