package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;

/**
 * Created by sokol on 2017-03-10.
 */
@Entity
public class MedicalVisitList extends Model {

    @Id
    public Long id;

    @Temporal(TemporalType.DATE)
    public Date date;

    @Temporal(TemporalType.TIME)
    public Date time;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    public Doctor doctor;

    public static Finder<Long, MedicalVisitList> find = new Finder<Long, MedicalVisitList>(MedicalVisitList.class);

    public static List<MedicalVisitList> findByDoctor(Long id) {
        return find.where()
                .eq("doctor_id",
                        id)
                .findList();
    }
}
