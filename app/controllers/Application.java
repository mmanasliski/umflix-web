package controllers;

import exception.CancelActionException;
import play.*;
import play.data.*;
import play.data.validation.Constraints.*;
import play.mvc.*;
import views.html.*;

import static play.data.Form.*;

public class Application extends Controller {

    private static final String EMAIL_ERROR ="Email already used, please try another";
    private static final String PASSWORD_MISSMATCH ="The passwords you inserted did not match";
    private static final String SUCCEDED =" created. Please head back to login page and sign in !";
    private static final String SUCCEDED_TO_CHANGE_PASSWORD ="You have updated your password succesfully";
    static  String token=""; //poner constante con invalid

    /**
     * Describes the login form.
     */
    public static class Login {
        public String name;
        public String password;
    }

    /**
     * Describes the Register form.
     */
    public static class Register {
        public String email;
        public String name;
        public String password;
        public String secondPassword;
    }
    public static class PasswordChanger{
        public String oldPassword;
        public String newPassword;
        public String newPasswordConfirm;
    }

    public static Result index() {
        return ok(index.render(form(Login.class)));
    }

    public static Result registerForm(){
        return ok(registerIndex.render(form(Register.class)));
    }
    public static Result changePassword(){
        return ok(changePasswordIndex.render(form(PasswordChanger.class)));
    }

    public static Result login(){
        UserController userController= new UserController();  //cambiar por metodo getBean???
        String message;
        Form<Login> form = form(Login.class).bindFromRequest();
        Login data = form.get();
        try{
            message = "WELCOME";
            userController.login(data.name, data.password);
            return ok(homePage.render(message));
        }catch(CancelActionException e){
            return unauthorized(e.getMessage());
        }
    }


    public static Result register(){
        //something
        return ok(homePage.render("Register"));
    }

    public static Result registering(){
        String message;
        Form<Register> form= form(Register.class).bindFromRequest();
        Register information = form.get();

        //intentar persistir informacion. Debe ir en User Controller
        if(information.password.equals(information.secondPassword)){
            try {
                userController.register(information.name,information.email,information.password);
                message="Account "+information.email+" created. Please go back and login to start watching!";
                return ok(register.render(message));
            } catch (CancelActionException e) {
                return ok(registerIndex.render(form(Register.class)));
            }
        }else{
            message=PASSWORD_MISSMATCH;
            return ok(registerIndex.render(form(Register.class)));
        }
    }

    public static Result changeMyPassword(){
        String message;
        Form<PasswordChanger> form = form(PasswordChanger.class).bindFromRequest();
        PasswordChanger information = form.get();
        if(information.newPassword.equals(information.newPasswordConfirm)){
            if(userController.updatePassword(information.oldPassword,information.newPassword)){
                message=SUCCEDED_TO_CHANGE_PASSWORD;
                return ok(changePassword.render(message));
            } else{
                return ok(changePasswordIndex.render(form(PasswordChanger.class)));
            }
        }else{
            return ok(changePasswordIndex.render(form(PasswordChanger.class)));
        }

    }
}
