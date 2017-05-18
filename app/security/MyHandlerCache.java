package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sokol on 2017-03-30.
 */

@Singleton
public class MyHandlerCache implements HandlerCache {

    private final DeadboltHandler defaultHandler;

    @Inject
    public MyHandlerCache(final ExecutionContextProvider ecProvider) {
        defaultHandler = new MyDeadboltHandler(ecProvider);
    }

    @Override
    public DeadboltHandler apply(String s) {
        return defaultHandler;
    }

    @Override
    public DeadboltHandler get() {
        return defaultHandler;
    }
}
