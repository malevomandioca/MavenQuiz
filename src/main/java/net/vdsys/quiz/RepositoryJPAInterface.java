package net.vdsys.quiz;

import java.sql.SQLException;

public interface RepositoryJPAInterface {

    public <T> T find(Class<T> t, int id) ;

}
