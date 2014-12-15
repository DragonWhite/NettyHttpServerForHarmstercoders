package ua.net.la.dragonwhite.httpserver.tools;

import java.lang.annotation.*;

@Target(value= ElementType.TYPE)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Mapped {
    String uri();
}