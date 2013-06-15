package ControllersTests;

import java.util.List;
import java.util.ResourceBundle;

import controllers.MoviePlayerController;
import controllers.UserController;
import edu.um.arq.umflix.catalogservice.CatalogService;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.authenticationhandler.impl.AuthenticationHandlerImpl;
import edu.umflix.model.Movie;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.*;
import exception.CancelActionException;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
            this.catalogService=Mockito.mock(CatalogService.class);
        }
    }



    @Test
    public void testLogin(){
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="hola@gmail.com";
        String password="password";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            testedMock.login(userEmail,password);
        } catch (CancelActionException e) {
        }
        try {
            verify(testedMock.userManager, times(1)).login(user);
        } catch (InvalidUserException e) {
        }

    }

    @Test
    public void testLoginInvalidUser(){
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        UserController testedMock= new UserControllerMock();
        String userEmail="noexisto@gmail.com";
        String password="password";
        User user = new User(userEmail, null, password, roleUser);
        try {
            doThrow(new edu.umflix.usermanager.exceptions.InvalidUserException()).when(testedMock.userManager).login(user);
        } catch (InvalidUserException e2) {
            try {
                testedMock.login(userEmail,password);
            } catch (Exception e) {
                assertTrue(e instanceof CancelActionException);
            }
        }
    }

    @Test
    public void testRegisterNewUser(){
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="nuevo@gmail.com";
        String name="Nuevito";
        String password="soynuevo";
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        User user = new User(userEmail, name, password, roleUser);
        try {
            testedMock.register(userEmail,name,password);
        } catch (CancelActionException e) {
        }
        try {
            verify(testedMock.userManager,times(1)).register(user);
        } catch (Exception e) {
        }
    }

    @Test
    public void testRegisterInvalidEmail(){
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        UserController testedMock= new UserControllerMock();
        String userEmail="invalido@gmail.com";
        String password="password";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new InvalidEmailException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            try {
                testedMock.register(name,userEmail,password);
            } catch (Exception e2) {
                assertTrue(e2 instanceof CancelActionException);
                assertTrue(e2.getMessage().equals(INVALID_EMAIL));
            }
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
    }

    @Test
    public void testRegisterInvalidPassword() {
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        UserController testedMock= new UserControllerMock();
        String userEmail="valido@gmail.com";
        String password="";
        String name="nombre";
        User user = new User(userEmail, name, password, roleUser);
        try {
            doThrow(new InvalidPasswordException()).when(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            try {
                testedMock.register(name,userEmail,password);
            } catch (Exception e2) {
                assertTrue(e2 instanceof CancelActionException);
                assertTrue(e2.getMessage().equals(INVALID_PASSWD));
            }
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }

    }
    @Test
    public void testRegisterInvalidRole() {
        Role roleUser = new Role(Role.RoleType.USER.getRole());
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
            try {
                testedMock.register(name,userEmail,password);
            } catch (Exception e2) {
                assertTrue(e2 instanceof CancelActionException);
                assertTrue(e2.getMessage().equals(INVALID_ROLE));
            }
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
    }
    @Test
    public void testRegisterEmailTaken() {
        Role roleUser = new Role(Role.RoleType.USER.getRole());
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
            try {
                testedMock.register(name, userEmail, password);
            } catch (Exception e2) {
                assertTrue(e2 instanceof CancelActionException);
                assertTrue(e2.getMessage().equals(EMAIL_TAKEN));
            }
        }

    }

    @Test
    public void testUpdatePassword(){
        UserController testedMock= new UserControllerMock();
        String newPassword="nueva";
        String oldPassword="vieja";
        try {
            User user=testedMock.authHandler.getUserOfToken(testedMock.getToken());
            testedMock.updatePassword(oldPassword,newPassword);
            try {
                try {
                    verify(testedMock.userManager,times(1)).update(testedMock.getToken(),user,newPassword);
                } catch (PermissionDeniedException e) {
                    fail();
                } catch (InvalidPasswordException e) {
                    fail();
                }
            } catch (InvalidUserException e) {
                fail();
            }
        } catch (InvalidTokenException e) {
            fail();
        }
    }

    @Test
    public void testNullPasswordUpdate(){
        UserController testedMock=new UserControllerMock();
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        String userEmail="stored@gmail.com";
        String password="secreta";
        String name="nombre";
        User storedUser=new User(userEmail,name,password,roleUser);
        when(testedMock.authHandler.validateToken("userToken")).thenReturn(true);
        try {
            when(testedMock.authHandler.getUserOfToken("userToken")).thenReturn(storedUser);
        } catch (InvalidTokenException e) {
        }
        try {
            doThrow(new InvalidPasswordException()).when(testedMock.userManager).update("userToken", storedUser, null);
        } catch (InvalidUserException e) {
            try {
                testedMock.updatePassword(password, null);
            } catch (Exception e2) {
                assertTrue(e2 instanceof InvalidPasswordException);
            }
        } catch (PermissionDeniedException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidTokenException e) {
            fail();
        }
    }
    @Test
    public void testNullUserUpdate(){
        UserController testedMock=new UserControllerMock();
        String password="secreta";
        String newPassword="new";
        User storedUser=null;
        when(testedMock.authHandler.validateToken("userToken")).thenReturn(true);
        try {
            when(testedMock.authHandler.getUserOfToken("userToken")).thenReturn(storedUser);
        } catch (InvalidTokenException e) {
            fail();
        }
        try {
            doThrow(new edu.umflix.usermanager.exceptions.InvalidUserException()).when(testedMock.userManager).update("userToken", null, newPassword);
        } catch (InvalidUserException e) {
            assertTrue(testedMock.updatePassword(null, newPassword) == false);
        } catch (PermissionDeniedException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidTokenException e) {
            fail();
        }
    }



    @Test
    public void testShowMovies(){
        UserController testedMock=new UserControllerMock();
        when(testedMock.getToken()).thenReturn("tokenValido");
        try {
            List<Movie> movieList = testedMock.showMovies();
        } catch (InvalidTokenException e) {
            fail();
        }
        try {
            verify(testedMock.catalogService, times(1)).search(null,testedMock.getToken());
        } catch (InvalidTokenException e) {
            fail();
        }
    }
    @Test
    public void testShowMoviesInvalidToken(){
        UserController testedMock=new UserControllerMock();
        when(testedMock.getToken()).thenReturn(null);
        try {
            doThrow(new InvalidTokenException()).when(testedMock.catalogService).search(null,testedMock.getToken());
        } catch (InvalidTokenException e) {
        }
        try {
            List<Movie> movieList = testedMock.showMovies();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTokenException);
        }
        try {
            testedMock.catalogService.search(null,testedMock.getToken());
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTokenException);
        }
    }
    @Test
    public void testChooseMovie(){
        UserController testedMock=new UserControllerMock();
        MoviePlayerController moviePlayerController = new MoviePlayerController();
        try {
            testedMock.chooseMovie((long)123);
        } catch (CancelActionException e) {
        }
        try {
            verify(moviePlayerController,times(1)).startMovie((long)123,testedMock.getToken());
        } catch (CancelActionException e1) {

        }
    }
}
