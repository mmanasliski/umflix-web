package ControllersTests;

import java.util.ResourceBundle;

import controllers.MoviePlayerController;
import controllers.UserController;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.authenticationhandler.impl.AuthenticationHandlerImpl;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.EmailAlreadyTakenException;
import edu.umflix.usermanager.exceptions.InvalidEmailException;
import edu.umflix.usermanager.exceptions.InvalidPasswordException;
import edu.umflix.usermanager.exceptions.InvalidRoleException;
import exception.CancelActionException;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    /*
+     *
+     * This class mocks UserController so it uses mocked classes for the UserManager's methods implementation
+     *
+     */

    private static final String INVALID_EMAIL ="The email you submited is not valid.";
    private static final String INVALID_PASSWD ="Please choose a valid password";
    private static final String INVALID_ROLE ="Please retry the registration process.";
    private static final String EMAIL_TAKEN ="Email already taken, choose another one";

    class UserControllerMock extends UserController {
        AuthenticationHandler getAuthHandler() {
            return authHandler;
        }
        UserManager getUserManager() {
            return userManager;
        }

        public UserControllerMock(){
            this.authHandler=Mockito.mock(AuthenticationHandlerImpl.class);
            this.userManager=Mockito.mock(UserManager.class);
        }
    }



    @Test
    public void testLogin(){
        Role roleUser = new Role((long)0);
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="hola@gmail.com";
        String password="password";
        User user = new User(userEmail, null, password, roleUser);
        try {
            testedMock.login(userEmail,password);
        } catch (CancelActionException e) {
            fail();
        }
        try {
            verify(testedMock.userManager).login(user);
        } catch (InvalidUserException e) {
            fail();
        }

    }

    @Test
    public void testLoginInvalidUser(){
        Role roleUser = new Role((long)0);
        UserController testedMock= new UserControllerMock();
        String userEmail="noexisto@gmail.com";
        String password="password";
        User user = new User(userEmail, null, password, roleUser);
        try {
            doThrow(new InvalidUserException()).when(testedMock.userManager).login(user);
        } catch (InvalidUserException e) {
            fail();
        }
        try {
            testedMock.login(userEmail,password);
        } catch (Exception e) {
            assertTrue(e instanceof CancelActionException);
        }
    }

    @Test
    public void testRegisterNewUser(){
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="nuevo@gmail.com";
        String name="Nuevito";
        String password="soynuevo";
        Role roleUser = new Role((long)0);
        User user = new User(userEmail, name, password, roleUser);
        try {
            testedMock.register(userEmail,name,password);
        } catch (CancelActionException e) {
            fail();
        }
        try {
            verify(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
    }

    @Test
    public void testRegisterInvalidEmail(){
        Role roleUser = new Role((long)0);
        UserController testedMock= new UserControllerMock();
        String userEmail="invalido@gmail.com";
        String password="password";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new InvalidEmailException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
        try {
            testedMock.register(name,userEmail,password);
        } catch (Exception e) {
            assertTrue(e instanceof CancelActionException);
            assertTrue(e.getMessage().equals(INVALID_EMAIL));
        }
    }

    @Test
    public void testRegisterInvalidPassword() {
        Role roleUser = new Role((long)0);
        UserController testedMock= new UserControllerMock();
        String userEmail="valido@gmail.com";
        String password="";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new InvalidEmailException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
        try {
            testedMock.register(name,userEmail,password);
        } catch (Exception e) {
            assertTrue(e instanceof CancelActionException);
            assertTrue(e.getMessage().equals(INVALID_PASSWD));
        }
    }
    @Test
    public void testRegisterInvalidRole() {
        Role roleUser = new Role((long)8);
        UserController testedMock= new UserControllerMock();
        String userEmail="valido@gmail.com";
        String password="secreta";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new InvalidRoleException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
        try {
            testedMock.register(name,userEmail,password);
        } catch (Exception e) {
            assertTrue(e instanceof CancelActionException);
            assertTrue(e.getMessage().equals(INVALID_ROLE));
        }
    }
    @Test
    public void testRegisterEmailTaken() {
        Role roleUser = new Role((long)0);
        UserController testedMock= new UserControllerMock();
        String userEmail="valido@gmail.com";
        String password="secreta";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new EmailAlreadyTakenException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
        try {
            testedMock.register(name,userEmail,password);
        } catch (Exception e) {
            assertTrue(e instanceof CancelActionException);
            assertTrue(e.getMessage().equals(EMAIL_TAKEN));
        }
    }
}
