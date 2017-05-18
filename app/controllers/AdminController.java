package controllers;

import java.util.*;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import org.w3c.dom.Document;
import play.Routes;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import views.html.*;

import javax.inject.Inject;


import static play.mvc.Controller.request;
import static play.mvc.Controller.response;

import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by sokol on 2017-03-10.
 */
public class AdminController {

    @Inject
    FormFactory formFactory;

    @Inject
    private HttpExecutionContext ec;

    @Restrict(@Group("admin"))
    public CompletionStage<Result> specializations() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<Specialization> specializationList = Specialization.find.all();
        return CompletableFuture.supplyAsync(() -> (ok(admin_specializations.render(specializationList, user))), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> addSpecialization() {
        JsonNode parameters = request().body().asJson();
        String name = parameters.get("name").asText();
        System.out.println(name);
        Specialization spec = new Specialization();
        spec.specializationName = name;
        spec.save();
        return CompletableFuture.supplyAsync(() -> (ok()), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> editSpecialization() {
        JsonNode parameters = request().body().asJson();
        String name = parameters.get("name").asText();
        Long id = parameters.get("id").asLong();
        Specialization spec = Specialization.find.byId(id);
        spec.specializationName = name;
        spec.update();
        return CompletableFuture.supplyAsync(() -> (ok()), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> addDoctor() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<Specialization> specializationList = Specialization.find.all();
        return CompletableFuture.supplyAsync(() -> (ok(admin_doctors_add.render(specializationList, user))), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> submitAddDoctor() {
        DynamicForm form = formFactory.form().bindFromRequest();
        User user = new User();
        Doctor doctor = new Doctor();
        SecurityRole role = SecurityRole.findByName("doctor");
        doctor.specialization = Specialization.getSpecialization(form.get("medicalSpecialization"));
        doctor.firstName = form.get("firstName");
        doctor.lastName = form.get("lastName");
        doctor.mondayHours = form.get("mondayHours");
        doctor.tuesdayHours = form.get("tuesdayHours");
        doctor.wednesdayHours = form.get("wednesdayHours");
        doctor.thursdayHours = form.get("thursdayHours");
        doctor.fridayHours = form.get("fridayHours");
        user.roles = new ArrayList<>();
        user.roles.add(role);
        user.username = form.get("username");
        user.password = form.get("password");
        user.doctor = doctor;
        doctor.save();
        user.save();

        return CompletableFuture.supplyAsync(() -> (redirect("/admin/doctors/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> doctors() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<User> doctors = User.getDoctors();
        return CompletableFuture.supplyAsync(() -> (ok(admin_doctors.render(doctors, user))), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> patients() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<User> patients = User.getPatients();
        return CompletableFuture.supplyAsync(() -> (ok(admin_patients.render(patients, user))), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> visits() {
        String username = Http.Context.current().session().get("username");
        User user = User.findByUserName(username);
        List<MedicalVisit> visits = MedicalVisit.find.findList();
        return CompletableFuture.supplyAsync(() -> (ok(admin_visits.render(visits, user))), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> doctorActivation(Long id) {
        User user = User.find.byId(id);
        user.active = true;
        user.update();
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/doctors/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> doctorDeactivation(Long id) {
        User user = User.find.byId(id);
        user.active = false;
        user.update();
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/doctors/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> patientActivation(Long id) {
        User user = User.find.byId(id);
        user.active = true;
        user.update();
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/patients/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> patientDeactivation(Long id) {
        User user = User.find.byId(id);
        user.active = false;
        user.update();
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/patients/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> deleteVisit(Long id) {
        MedicalVisit.find.deleteById(id);
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/visits/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> deleteDoctor(Long id) {
        User.find.deleteById(id);
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/doctors/get")), ec.current());
    }

    @Restrict(@Group("admin"))
    public CompletionStage<Result> deletePatient(Long id) {
        User.find.deleteById(id);
        return CompletableFuture.supplyAsync(() -> (redirect("/admin/patients/get")), ec.current());
    }
}
