package net.vdsys.quiz;

import java.sql.SQLException;

public interface QuizRepositoryInterface {

    public Quiz findQuiz(String quizName) throws quizIOException, quizFileNotFoundException, quizSqlException;



}
