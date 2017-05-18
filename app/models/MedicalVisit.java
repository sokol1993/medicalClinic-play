package models;

import java.util.*;
import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by sokol on 2017-03-10.
 */
@Entity
public class MedicalVisit extends Model{

    @Id
    public Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    public Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    public Doctor doctor;

    @Temporal(TemporalType.DATE)
    public Date date;

    @Temporal(TemporalType.TIME)
    public Date time;

    public boolean isCompleted = false;

    public static Finder<Long, MedicalVisit> find = new Finder<Long, MedicalVisit>(MedicalVisit.class);

    public static List<MedicalVisit> findByDoctor(Long id) {
        return find.where()
                .eq("doctor_id",
                        id)
                .findList();
    }

    public static List<MedicalVisit> findByPatient(Long id) {
        return find.where()
                .eq("patient_id",
                        id)
                .findList();
    }
}
