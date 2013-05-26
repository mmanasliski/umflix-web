package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.data.validation.Constraints.*;

import java.util.*;

import views.html.*;

public class Application extends Controller {
    
    /**
     * Describes the hello form.
     */
    public static class Login {
        @Required public String name;
        @Required public String password;
    } 
    
    // -- Actions
  
    /**
     * Home page
     */
    public static Result index() {
        return ok(
            index.render(form(Login.class))
        );
    }
  
    /**
     * Handles the form submission.
     */
    public static Result log() {
        Form<Login> form = form(Login.class).bindFromRequest();
        if(form.hasErrors()) {
            return badRequest(index.render(form));
        } else {
            Login data = form.get();
            return ok(
                login.render(data.name, data.password)
            );
        }
    }
  
}
