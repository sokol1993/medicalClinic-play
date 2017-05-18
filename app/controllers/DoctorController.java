package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Doctor;
import models.MedicalVisit;
import models.MedicalVisitList;
import models.User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.doctor_edit;
import views.html.doctor_visits;
import views.html.doctor_visits_list_add;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by sokol on 2017-03-10.
 */
public class DoctorController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    private HttpExecutionContext ec;


    @Restrict(@Group("doctor"))
    public CompletionStage<Result> addVisitList() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        return CompletableFuture.supplyAsync(() -> (ok(doctor_visits_list_add.render(user))), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> submitAddVisitList() {
        DynamicForm form = formFactory.form().bindFromRequest();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        Date date = null;
        try {
            date = DATE_FORMAT.parse(form.get("dateVisit"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int a = Integer.parseInt(form.get("availableVisits"));
        Date tmp = date;
        for (int i = 0; i < a; i++) {
            MedicalVisitList list = new MedicalVisitList();
            list.doctor = user.doctor;
            list.date = date;
            if (i == 0) {
                tmp.setTime(tmp.getTime());
            } else {
                tmp.setTime(tmp.getTime() + 900000);
            }
            list.time = tmp;
            list.save();
        }
        return CompletableFuture.supplyAsync(() -> (redirect("/doctor/visits/get")), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> editDoctor() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        return CompletableFuture.supplyAsync(() -> (ok(doctor_edit.render(user))), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> submitEditDoctor() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);

        user.doctor.mondayHours = form.get("mondayHours");
        user.doctor.tuesdayHours = form.get("tuesdayHours");
        user.doctor.wednesdayHours = form.get("wednesdayHours");
        user.doctor.thursdayHours = form.get("thursdayHours");
        user.doctor.fridayHours = form.get("fridayHours");

        user.password = form.get("password");

        user.doctor.update();
        user.update();

        return CompletableFuture.supplyAsync(() -> (redirect("/doctor/visits/get")), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> deleteVisitList(Long id) {
        MedicalVisitList.find.deleteById(id);
        return CompletableFuture.supplyAsync(() -> (redirect("/doctor/visits/get")), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> confirmVisit(Long id) {
        MedicalVisit medicalVisit = MedicalVisit.find.byId(id);
        medicalVisit.isCompleted = true;
        medicalVisit.update();
        return CompletableFuture.supplyAsync(() -> (redirect("/doctor/visits/get")), ec.current());
    }

    @Restrict(@Group("doctor"))
    public CompletionStage<Result> visits() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<MedicalVisit> visits = MedicalVisit.findByDoctor(user.doctor.id);
        List<MedicalVisitList> visitLists = MedicalVisitList.findByDoctor(user.doctor.id);

        return CompletableFuture.supplyAsync(() -> (ok(doctor_visits.render(visits, visitLists, user))), ec.current());
    }
}
