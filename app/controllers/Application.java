package controllers;

import exception.CancelActionException;
import play.*;
import play.data.*;
import play.data.validation.Constraints.*;
import play.mvc.*;
import views.html.*;

import static play.data.Form.*;

public class Application extends Controller {
  @EJB(beanName = "UserController")
    UserController userController;      //cambiar por metodo getBean???

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
        try{
            userController.login(data.name,data.password);
            message = "WELCOME";
        }catch(CancelActionException e){
            message="ERROR";
        }
        return ok(homePage.render(message));
    }
}
