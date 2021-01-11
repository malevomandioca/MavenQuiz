package net.vdsys.quiz;

        import com.fasterxml.jackson.databind.ObjectMapper;

        import java.sql.*;
        import java.io.FileNotFoundException;
        import java.io.IOException;

public class QuizJDBCRepository implements QuizRepositoryInterface {


    @Override
    public Quiz findQuiz(String quizName) throws quizSqlException {

        Statement statement = null;

        try {
            String sqlconnstr = "jdbc:sqlserver://develop;user=vdadmin;password=vd12";

            Connection conn = DriverManager.getConnection(sqlconnstr);
            if (conn != null) {
                System.out.println("Connected");
            }
            ResultSet rs = statement.executeQuery("SELECT * FROM Query WHERE Name = '"+quizName+"'");
            rs.next();
            //crear objeto con los datos del queri

            return null;
        } catch (SQLException fnfe) {
            throw new quizSqlException(fnfe.getMessage());
        }


    }



}
