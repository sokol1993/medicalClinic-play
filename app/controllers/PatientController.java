package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.sun.org.apache.regexp.internal.RE;
import it.innove.play.pdf.PdfGenerator;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by sokol on 2017-03-10.
 */
public class PatientController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    private HttpExecutionContext ec;

    @Inject
    public PdfGenerator pdfGenerator;

    @Restrict(@Group("patient"))
    public CompletionStage<Result> edit() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        return CompletableFuture.supplyAsync(() -> (ok(patient_edit.render(user))), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> submitEdit() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        DynamicForm form = formFactory.form().bindFromRequest();

        String password = form.get("password");
        user.password = password;
        String email = form.get("email");
        user.patient.email = email;

        String streetName = form.get("streetName");
        user.patient.streetName = streetName;
        String streetNumber = form.get("streetNumber");
        user.patient.streetNumber = streetNumber;
        String homeNumber = form.get("homeNumber");
        user.patient.homeNumber = homeNumber;
        String postcode = form.get("postcode");
        user.patient.postcode = postcode;
        String city = form.get("city");
        user.patient.city = city;

        user.patient.update();
        user.update();

        return CompletableFuture.supplyAsync(() -> (redirect("/patient/visits/get")), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> addVisit(Long idDoctor) {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<MedicalVisitList> medicalVisitListList = MedicalVisitList.findByDoctor(idDoctor);
        Doctor doctor = Doctor.find.byId(idDoctor);
        return CompletableFuture.supplyAsync(() -> (ok(patient_visit_add.render(medicalVisitListList, doctor, user))), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> submitAddVisit() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        MedicalVisitList medicalVisitList = MedicalVisitList.find.byId(Long.parseLong(form.get("idVisit")));

        MedicalVisit medicalVisit = new MedicalVisit();
        medicalVisit.date = medicalVisitList.date;
        medicalVisit.time = medicalVisitList.time;
        medicalVisit.doctor = medicalVisitList.doctor;
        medicalVisit.patient = user.patient;
        medicalVisit.save();

        MedicalVisitList.find.deleteById(medicalVisitList.id);

        return CompletableFuture.supplyAsync(() -> (redirect("/patient/visits/get")), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> doctors() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<User> doctors = User.getDoctors();
        return CompletableFuture.supplyAsync(() -> (ok(patient_doctors_list.render(doctors, user))), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> doctor(Long id) {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        Doctor doctor = Doctor.find.byId(id);
        return CompletableFuture.supplyAsync(() -> (ok(patient_doctor_detail.render(doctor, user))), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> visits() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<MedicalVisit> visits = MedicalVisit.findByPatient(user.patient.id);
        return CompletableFuture.supplyAsync(() -> (ok(patient_visits.render(visits, user))), ec.current());
    }

    @Restrict(@Group("patient"))
    public CompletionStage<Result> billForVisit(Long idPatient, Long idDoctor, Long idVisit) {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);

        MedicalVisit visit = MedicalVisit.find.byId(idVisit);
        if (visit != null && visit.patient.id == idPatient && visit.doctor.id == idDoctor && visit.isCompleted) {
            return CompletableFuture.supplyAsync(() -> (pdfGenerator.ok(patient_visit_bill.render(visit, user), "http://localhost:9000/patient/bill")), ec.current());
        } else {
            return CompletableFuture.supplyAsync(() -> (ok(patient_edit.render(user))), ec.current());
        }

    }

}
