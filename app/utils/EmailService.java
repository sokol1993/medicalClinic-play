package utils;

import models.User;
import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;

import javax.inject.Inject;

/**
 * Created by sokol on 2017-04-13.
 */
public class EmailService {
    @Inject
    MailerClient mailerClient;

    public void sendEmail(User user) {
        Email email = new Email()
                .setSubject("Rejestracja w przychodniu")
                .setFrom("przychodnia@gmail.com")
                .addTo(user.patient.email)
                .setBodyText("A text message")
                .setBodyHtml("<h1>Witaj " + user.patient.firstName + " " + user.patient.lastName + "</h1><br><h3>" +
                        "W celu dokończenia rejestracji kliknij w poniższy link</h3> <br>" +
                        "<a href=http://localhost:9000/activation/" + user.token.token + "/" + user.token.id + "");
        mailerClient.send(email);
    }

}
