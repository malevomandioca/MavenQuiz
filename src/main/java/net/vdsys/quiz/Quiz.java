package net.vdsys.quiz;

import javax.management.Query;
import java.util.ArrayList;

public class Quiz {

       private String name;
    private String topic;
    private ArrayList<Question> questions;
    private boolean evaluation;
    private User user;

    public Quiz() {
        this.topic = "";
        this.user = null;
        this.evaluation = false;
        this.questions = new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getTopic() {
        return topic;
    }

    public void setTopic(String q) {
        this.topic = q;
    }

    public boolean getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(boolean e) {
        this.evaluation = e;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }



    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public String getUserData() {
        if (user != null && !user.getName().equals("")) {
            return  user.getName() + " de " + user.getAge() + " años de edad.";
        } else {
            return "ANONIMO.";
        }
    }

    public String getUserDataForExport() {
        if (user != null && !user.getName().equals("")) {
            return  user.getName() + "," + user.getAge();
        } else {
            return "ANONIMO,0";
        }
    }

}
