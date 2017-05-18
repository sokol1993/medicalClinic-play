package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.models.Subject;
import models.User;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import views.html.defaultpages.unauthorized;
import views.html.success;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static play.mvc.Results.ok;

/**
 * Created by sokol on 2017-03-30.
 */
public class MyDeadboltHandler extends AbstractDeadboltHandler {

    public MyDeadboltHandler(ExecutionContextProvider ecProvider) {
        super(ecProvider);
    }

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(Http.Context context) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public CompletionStage<Optional<? extends Subject>> getSubject(Http.Context context) {
        String username = Http.Context.current().session().get("username");
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(User.findByUserName(username)), (Executor) executionContextProvider.get());
    }

    @Override
    public CompletionStage<Result> onAuthFailure(Http.Context context, Optional<String> content) {
        return CompletableFuture.completedFuture(redirect("/authFailure"));
    }

    @Override
    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.Context context) {
        return null;
    }
}
