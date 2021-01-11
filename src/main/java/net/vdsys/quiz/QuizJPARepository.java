package net.vdsys.quiz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

public class QuizJPARepository implements RepositoryJPAInterface{

    //private static EntityManagerFactory entityManagerFactory;
   // private EntityManager entityManager = null;

    public QuizJPARepository(){
        //entityManagerFactory = Persistence.createEntityManagerFactory("db");
    }

    public <T> T find(Class<T> t, int id) {
        //this.entityManager = entityManagerFactory.createEntityManager();
       // T o = this.entityManager.find(t, id);
       // this.entityManager.close();
        return null;
    }






}
