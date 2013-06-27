package controllers;

import edu.um.arq.umflix.catalogservice.CatalogService;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.exceptions.ActivityNotFoundException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.*;
import edu.umflix.persistence.ActivityDao;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.*;
import model.MovieManager;
import model.exceptions.NoAdsException;
import model.exceptions.UserNotAllowedException;
import model.exceptions.ValuesInActivityException;
import org.apache.commons.io.IOUtils;

import javax.naming.InitialContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * Bean Factory
 */
public class BeanFactory {

    public static final String MOVIE_MANAGER = "MovieManager";
    public static final String USER_MANAGER = "UserManager";
    public static final String AUTH_HANDLER = "AuthenticationHandler";
    public static final String CATALOG_SERVICE = "CatalogService";
    public static final String ACTIVITY_DAO = "ActivityDao";


    public static Object getBean(String key){
        final Clip c = new Clip(10L,1);
        c.setId(1L);

        try{
//            Properties p = new Properties();
            Object o = null;
//            p.put("java.naming.factory.initial", "org.apache.openejb.client.RemoteInitialContextFactory");
//            p.put("java.naming.provider.url", "ejbd://localhost:4201");
//            InitialContext ctx = new InitialContext(p);
            if(key.equals(MOVIE_MANAGER)){

                o = new MovieManager() {
                    @Override
                    public List<Clip> getMovie(String s, Long aLong) throws InvalidTokenException, MovieNotFoundException, UserNotAllowedException {
                     List<Clip> lista = new LinkedList<Clip>();
                        lista.add(c);
                        return lista;
                    }

                    @Override
                    public ClipData getClipData(String s, Long aLong) throws InvalidTokenException {
                        FileInputStream fs = null;
                        try {
                            fs = new FileInputStream("C:\\Obligatorio\\umflix-web\\app\\video\\video.mp4");
                            byte[] bytes = IOUtils.toByteArray((InputStream) fs);
                            MoviePlayerController moviePlayerController = new MoviePlayerController();
                            Byte[] bytes2 = new Byte[bytes.length];
                            for (int i=0;i<bytes.length;i++){
                                bytes2[i]= new Byte(bytes[i]);
                            }
                           ClipData clipData = new ClipData(bytes2,c);
                            return clipData;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            throw new InvalidTokenException();
                        } catch (IOException e) {
                            throw new InvalidTokenException();
                        }
                    }

                    @Override
                    public void sendActivity(String s, Activity activity) throws InvalidTokenException, ValuesInActivityException, UserNotAllowedException {

                    }

                    @Override
                    public ClipData getAd(String s, Long aLong) throws InvalidTokenException, NoAdsException {
                        FileInputStream fs = null;
                        try {
                            fs = new FileInputStream("video/video.mp4");
                            byte[] bytes = IOUtils.toByteArray((InputStream) fs);
                            MoviePlayerController moviePlayerController = new MoviePlayerController();
                            Byte[] bytes2 = new Byte[bytes.length];
                            for (int i=0;i<bytes.length;i++){
                                bytes2[i]= new Byte(bytes[i]);
                            }
                            ClipData clipData = new ClipData(bytes2,c);
                            return clipData;
                        } catch (FileNotFoundException e) {
                            throw new InvalidTokenException();
                        } catch (IOException e) {
                            throw new InvalidTokenException();
                        }
                    }
                };
            }
            if(key.equals(USER_MANAGER)){
                o = new UserManager() {
                    @Override
                    public void register(User user) throws InvalidEmailException, InvalidPasswordException, InvalidRoleException, EmailAlreadyTakenException {

                    }

                    @Override
                    public String login(User user) throws InvalidUserException {
                        return "usertoken";
                    }

                    @Override
                    public void delete(String s, User user) throws InvalidUserException, PermissionDeniedException, InvalidTokenException {

                    }

                    @Override
                    public void update(String s, User user, String s2) throws InvalidUserException, PermissionDeniedException, InvalidPasswordException, InvalidTokenException {

                    }
                }             ;
            }
            if(key.equals(AUTH_HANDLER)){
                o = new AuthenticationHandler() {
                    @Override
                    public boolean validateToken(String s) {
                        return s.equals("usertoken");
                    }

                    @Override
                    public String authenticate(User user) throws InvalidUserException {
                        //if (user.getEmail().equals("useremail@mail.com")){
                            return "usertoken";
//                        } else {
//                            throw new InvalidUserException();
//                        }
                    }

                    @Override
                    public User getUserOfToken(String s) throws InvalidTokenException {
                        //if (s.equals("usertoken")){
                            User u = new User();
                            u.setEmail("useremail@mail.com");
                            return u;
                        //}  else {
                        //    throw new InvalidTokenException();
                        //}
                    }

                    @Override
                    public boolean isUserInRole(String s, Role role) throws InvalidTokenException {
                        return false;
                    }
                }    ;
            }
            if(key.equals(CATALOG_SERVICE)){
            o = new CatalogService() {
                @Override
                public List<Movie> search(String s, String s2) throws InvalidTokenException {
                    List<String> cast = new LinkedList<String>();
                    cast.add("perro");
                    List<Clip> clips = new LinkedList<Clip>();
                    clips.add(c);
                    List<Movie> movies = new LinkedList<Movie>();
                    Movie movie = new Movie(cast,clips,"Spielberg",12L,true,"Comedy",null,null,"EL perro loco");
                    movie.setId(1L);
                    movies.add(movie);
                    return movies;
                    }
                };
            }
            if(key.equals(ACTIVITY_DAO)){
                o = new ActivityDao() {
                    @Override
                    public void addActivity(Activity activity) {

                    }

                    @Override
                    public Activity getActivityById(Long aLong) throws ActivityNotFoundException {
                       return new Activity();
                    }

                    @Override
                    public void updateActivity(Activity activity) throws ActivityNotFoundException {

                    }

                    @Override
                    public void deleteActivity(Long aLong) throws ActivityNotFoundException {

                    }
                }  ;
            }
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
