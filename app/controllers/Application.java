package controllers;

import play.*;
import play.data.*;
import play.data.validation.Constraints.*;
import play.mvc.*;
import views.html.*;

import static play.data.Form.*;

public class Application extends Controller {
    static  String token=""; //poner constante con invalid

    /**
     * Describes the login form.
     */
    public static class Login {
        public String name;
        public String password;
    }

    public static Result index() {
        return ok(index.render(form(Login.class)));
    }

    public static Result login(){
        String message;
        Form<Login> form = form(Login.class).bindFromRequest();
        Login data = form.get();

       if(data.name.equals("pepe") && data.password.equals("grillo")){
        message="welcome";
        }else{
          message="wrong";
        }

        return ok(homePage.render(message));
    }
}
