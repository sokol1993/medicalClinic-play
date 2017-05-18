package models;

import java.util.*;
import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Created by sokol on 2017-03-08.
 */
@Entity
public class Specialization extends Model{

    @Id
    public Long id;

    public String specializationName;

    @OneToMany(mappedBy = "specialization")
    public List<Doctor> doctorList;

    public static Finder<Long, Specialization> find = new Finder<Long, Specialization>(Specialization.class);

    public static Specialization getSpecialization(String name){
        List<Specialization> specializationList = find.all();
        for(Specialization s : specializationList){
            if(s.specializationName.equals(name)){
                return s;
            }
        }
        return null;
    }
}
