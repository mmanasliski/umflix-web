package controllers;

import edu.um.arq.umflix.catalogservice.CatalogService;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.usermanager.UserManager;
import model.MovieManager;

import javax.naming.InitialContext;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: telematica
 * Date: 14/06/13
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
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
                o = (MovieManager) ctx.lookup(MOVIE_MANAGER);
            }
            if(key.equals(USER_MANAGER)){
                o = (UserManager) ctx.lookup(USER_MANAGER);
            }
            if(key.equals(AUTH_HANDLER)){
                o = (AuthenticationHandler)ctx.lookup(AUTH_HANDLER);
            }
            if(key.equals(CATALOG_SERVICE)){
                o = (CatalogService)ctx.lookup(CATALOG_SERVICE);
            }
            return o;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
