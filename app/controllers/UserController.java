package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.regexp.internal.RE;
import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.EmailService;
import views.html.*;

import javax.inject.Inject;

/**
 * Created by sokol on 2017-03-07.
 */
public class UserController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    EmailService emailService;

    public Result register() {
        return ok(guest_register.render());
    }

    public Result authFailure() {
        return ok(authFailure.render());
    }

    public Result submitRegister() {
        DynamicForm form = formFactory.form().bindFromRequest();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        User user = new User();
        Patient patient = new Patient();
        SecurityRole role = SecurityRole.findByName("patient");
        String username = form.get("username");
        user.username = username;
        String password = form.get("password");
        user.password = password;
        String firstName = form.get("firstName");
        patient.firstName = firstName;
        String lastName = form.get("lastName");
        patient.lastName = lastName;
        String email = form.get("email");
        patient.email = email;
        Date dateOfBirth = null;
        try {
            dateOfBirth = df.parse(form.get("dateOfBirth"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        patient.dateOfBirth = dateOfBirth;
        String pesel = form.get("pesel");
        patient.pesel = pesel;
        String streetName = form.get("streetName");
        patient.streetName = streetName;
        String streetNumber = form.get("streetNumber");
        patient.streetNumber = streetNumber;
        String homeNumber = form.get("homeNumber");
        patient.homeNumber = homeNumber;
        String postcode = form.get("postcode");
        patient.postcode = postcode;
        String city = form.get("city");
        patient.city = city;
        user.roles = new ArrayList<>();
        user.roles.add(role);
        user.patient = patient;
        String uuid = UUID.randomUUID().toString();
        Token token = new Token();
        token.token = uuid;
        token.save();
        user.token = token;
        emailService.sendEmail(user);
        patient.save();
        user.save();

        return redirect("/guest/login");
    }

    public Result login() {
        Http.Context.current().session().clear();
        return ok(guest_login.render());
    }

    public Result submitLogin() {
        DynamicForm form = formFactory.form().bindFromRequest();
        String username = form.get("username");
        String password = form.get("password");

        User user = User.findByUserName(username);

        if (user != null && user.password.equals(password) && user.active) {
            Http.Context.current().session().put("username", username);
        }

        if (Http.Context.current().session().get("username") != null) {
            if (user.patient == null) {
                if (user.doctor == null) {
                    return redirect("/admin/doctors/get");
                } else {
                    return redirect("/doctor/visits/get");
                }
            } else {
                return redirect("/patient/visits/get ");
            }
        } else {
            return redirect("/authFailure ");
        }
    }

    public Result doctors() {
        List<User> doctors = User.getDoctors();
        return ok(guest_doctors_list.render(doctors));
    }

    public Result doctor(Long id) {
        Doctor doctor = Doctor.find.byId(id);
        return ok(guest_doctor_detail.render(doctor));
    }

    public Result polishLogin() {
        ctx().changeLang("pl");
        return redirect("/guest/login");
    }

    public Result englishLogin() {
        ctx().changeLang("en");
        return redirect("/guest/login");
    }

    public Result spanishLogin() {
        ctx().changeLang("es");
        return redirect("/guest/login");
    }

    public Result polishRegister() {
        ctx().changeLang("pl");
        return redirect("/guest/register");
    }

    public Result englishRegister() {
        ctx().changeLang("en");
        return redirect("/guest/register");
    }

    public Result spanishRegister() {
        ctx().changeLang("es");
        return redirect("/guest/register");
    }

    public Result activationUser(String tokenName, Long id) {
        Token token = Token.find.byId(id);
        List<User> users = User.find.all();
        if (token.token.equals(tokenName)) {
            for (User u : users) {
                if (u.token != null && u.token.id == token.id) {
                    u.active = true;
                    u.token = null;
                    u.update();
                    token.delete();
                }
            }
        }
        return redirect("/guest/login");
    }

    public Result checkUserExists() {
        JsonNode parameters = request().body().asJson();
        String name = parameters.get("name").asText();
        User user = User.findByUserName(name);
        if (user != null) {
            return status(406);
        } else {
            return status(200);
        }
    }
}
