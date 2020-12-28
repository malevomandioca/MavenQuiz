package net.vdsys.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QuizRepository {

    private final File directory;

    public QuizRepository(File directory) {
        this.directory = directory;
    }

    public Quiz findQuiz(String quizName) throws quizIOException, quizFileNotFoundException  {
        File f = new File(this.directory.getAbsoluteFile() + "\\" + quizName + ".json");
        ObjectMapper map = new ObjectMapper();
        try {
            return map.readValue(f, Quiz.class);
        } catch (FileNotFoundException fnfe) {
            throw new quizFileNotFoundException(fnfe.getMessage());
        }
        catch (IOException ioe) {
            throw new quizIOException(ioe.getMessage());
        }

    }


}
