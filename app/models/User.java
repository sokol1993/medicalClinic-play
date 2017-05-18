package models;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.*;

/**
 * Created by sokol on 2017-03-07.
 */
@Entity
@Table(name="users")
public class User extends Model implements Subject {

    @Id
    public Long id;

    public String username;
    public String password;

    public boolean active = false;

    @OneToOne
    @JoinColumn(name = "patient_id")
    public Patient patient;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    public Doctor doctor;

    @OneToOne
    @JoinColumn(name = "token_id")
    public Token token;

    @ManyToMany
    public List<SecurityRole> roles;

    @ManyToMany
    public List<UserPermission> permissions;

    public static Finder<Long, User> find = new Finder<Long, User>(User.class);

    public static List<User> getDoctors(){
        List<User> toRemove = new ArrayList<>();
        List<User> doctors = find.fetch("doctor").fetch("doctor.specialization").findList();
        for(User u : doctors){
            if(u.doctor == null){
                toRemove.add(u);
            }
        }
        doctors.removeAll(toRemove);
        return doctors;
    }

    public static List<User> getPatients(){
        List<User> patients = find.fetch("patient").findList();
        List<User> toRemove = new ArrayList<>();
        for(User u : patients){
            if(u.patient == null){
                toRemove.add(u);
            }
        }
        patients.removeAll(toRemove);
        return patients;
    }

    @Override
    public List<? extends Role> getRoles() {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String getIdentifier() {
        return username;
    }

    public static User findByUserName(String userName) {
        return find.where()
                .eq("username",
                        userName)
                .findUnique();
    }
}
