package net.vdsys.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Question {

    private String question;
    private ArrayList<Reply> options;
    private ArrayList<Reply> answers;


    public Question() {
        this.question = "";
        this.options = new ArrayList<>();
        this.answers = new ArrayList<>();
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String q) {
        this.question = q;
    }

    public ArrayList<Reply> getOptions() {
        return options;

    }

    public void addOption(Reply r) {
        options.add(r);
    }

    public void addAnswer(int a) {
        List<Reply> r =  options.stream().filter((o) -> o.getNumber() == a)
                .collect(Collectors.toList());
        answers.add(r.get(0));
    }

    public long isMultipleReply() {
        return options.stream().filter(Reply::getRight).count();
    }

    public String getOpciones() {
        String resp = "";
        if (this.question != null && !this.question.isEmpty() && this.options.size() > 0) {
            final String[] x = {""};
            options.forEach(r -> x[0] += r.getNumber() +" ->" +  r.getOption() + "\n");
            // options.stream().forEach(r -> { x[0] += String.valueOf(r.getNumber()) +" ->" +  r.getOption() + "\n";  });
            resp = Arrays.toString(x).replace("[","").replace("]","");
        }
        return resp;
    }

    public ArrayList<Reply> getAnswers() {
        return answers;
    }





}