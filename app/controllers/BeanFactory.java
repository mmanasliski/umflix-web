package controllers;

import edu.um.arq.umflix.catalogservice.CatalogService;
import edu.um.arq.umflix.catalogservice.exception.DaoException;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.*;
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


    public static Object getBean(String key){


        try{
            Properties p = new Properties();
            Object o = null;
            p.put("java.naming.factory.initial", "org.apache.openejb.client.RemoteInitialContextFactory");
            p.put("java.naming.provider.url", "ejbd://localhost:4201");
            InitialContext ctx = new InitialContext(p);
            if(key.equals(MOVIE_MANAGER)){
                final Clip c = new Clip(10L,1);
                c.setId(1L);
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

                    @Override
                    public void sendActivity(String s, Activity activity) throws InvalidTokenException, ValuesInActivityException, UserNotAllowedException {
                        //To change body of implemented methods use File | Settings | File Templates.
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
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public String login(User user) throws InvalidUserException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void delete(String s, User user) throws InvalidUserException, PermissionDeniedException, InvalidTokenException {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void update(String s, User user, String s2) throws InvalidUserException, PermissionDeniedException, InvalidPasswordException, InvalidTokenException {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                }             ;
            }
            if(key.equals(AUTH_HANDLER)){
                o = new AuthenticationHandler() {
                    @Override
                    public boolean validateToken(String s) {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public String authenticate(User user) throws InvalidUserException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public User getUserOfToken(String s) throws InvalidTokenException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public boolean isUserInRole(String s, Role role) throws InvalidTokenException {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                }    ;
            }
            if(key.equals(CATALOG_SERVICE)){
                o = new CatalogService() {
                    @Override
                    public List<Movie> search(String s, String s2) throws DaoException, InvalidTokenException {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
            }
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
