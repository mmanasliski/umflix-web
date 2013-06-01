package dao;

import exception.DaoException;
import org.apache.log4j.Logger;


/**
 *
 * Creates instances of implementations to the given interface
 *
 */
public class DaoFactory {

    private static Logger logger = Logger.getLogger("DaoFactory.class");

    public static Object getDao(String dao) throws DaoException {
        try {
            return Class.forName(dao).newInstance();
        } catch (Exception exc) {
            if(exc instanceof InstantiationException || exc instanceof IllegalAccessException ||
                    exc instanceof ClassNotFoundException || exc instanceof NoSuchFieldException ) {
                logger.error("Cannot getDao",exc);
                throw new DaoException();
            } else {
                throw new RuntimeException(exc);
            }
        }
    }
}
