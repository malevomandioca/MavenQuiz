package net.vdsys.quiz;

import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class QuizManager {

    private static String fileQuiz;
    private static String fileOutput;
    private static boolean evaluate;
    private static String evaluatePrefix;
    private static String evaluateDir;
    private static FileWriter writerOutput;
    private static Quiz quiz;
    private static String quizName;
    private static Scanner scanner;

    public static void main(String[] args)  {

        initVars() ;

        if (validateArgs(args)) {

            QuizRepository repo = new QuizRepository(new File(evaluateDir));
            try {
                Quiz q = repo.findQuiz(quizName);
            } catch (quizFileNotFoundException qnf) {
                qnf.printStackTrace();
                System.err.println(qnf.getLocalizedMessage());
                System.exit(1);
            } catch (quizIOException ioe) {
                // ioe.printStackTrace();
                System.err.println(ioe.getLocalizedMessage());
                System.exit(1);
            }


            try {
                if (!evaluate) {
                    // solicitar respuestas y generar archivo con resultados
                    File fi = new File(fileQuiz);
                    if (fi.exists()) {
                        //writerOutput = new FileWriter(fileOutput);
                        //f.write(fileOutput);
                        //f.close();
                        if (loadQuiz()) {
                            System.out.println((quiz.getEvaluation() ? "EVALUACION" : "ESTADISTICA") + " --> " + quiz.getTopic());
                            System.out.println("-----------");
                            fillUser();
                            System.out.println("Al contestar: si son mas de una respuesta, separar por comas; si no responde ingrese 0.");
                            System.out.println();
                            if (fillQuiz()) {
                                System.out.println("GRACIAS!");
                                save();
                            }
                        } else {
                            System.out.println("Error: no se pudo crear el cuestionario!");
                        }

                    } else {
                        System.out.println("Error en encuesta: el archivo " + fileQuiz + " no existe!");
                    }
                } else {
                    evaluate();
                }
            } catch (IOException ioe) {
                System.out.println("Error en archivos: IOExepcion:"+fileQuiz + " no se puede crear!");
                ioe.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error en archivos: Exepcion:"+fileQuiz + " no existe!");
                e.printStackTrace();
            }
        } else {
            System.out.println("""
                    Error en parámetros: para ingreso se espera: Quiz  new [nombre encuensta] [evaluacion/encuesta].
                                         para evaluar respuestas: Quiz eval [nombre encuensta] [directorio] [archivoslida].
                                                               o: Quiz eval [nombre encuensta] [directorio] [archivoslida].""");
        }

    }

    private static void initVars() {
        fileQuiz = "";
        fileOutput = "";
        writerOutput = null;
        quiz = null;
        scanner = new Scanner(System.in);
        evaluate = false;
        evaluatePrefix = "";
        evaluateDir = "";
    }

    private static boolean validateArgs(String[] args) {
      if (args != null && args.length == 2) {
            /* archivo entrada cuestionario */
            String fQuiz = args[0];
            String fOut = args[1];
            if (!fQuiz.equals("") && !fOut.equals("")) {
                fileQuiz = fQuiz;
                fileOutput = fOut;
                return true;
            } else {
                return false;
            }
        } else if (args != null && args.length == 3) {

            String p0 = args[0];
            String p1 = args[1];
            String p2 = args[2];
            if (p0.equals("eval")) {
                /* evaluate qui, loaded from json */
                evaluate = true;
                quizName = args[1];
                evaluateDir = args[2];
            } else if (p0.equals("asdasd")) {
                /* archivo salida respuestas */
                evaluate = true;
                evaluateDir = args[0];
                evaluatePrefix = args[1];
                fileQuiz = args[2];
            }
            return true;
        } else {
            return false;
        }


    }

    private static boolean loadQuiz() throws IOException {
        quiz = new Quiz();
        try {
            File fq = new File(fileQuiz);
            BufferedReader rdr = new BufferedReader(new FileReader(fq));
            String line;
            int lineid = 0;
            int AnswerNumber = 1;
            Question q = null;
            while ((line = rdr.readLine()) != null) {
                if (lineid == 0) {
                    // VEO SI ES ESTADISTICA O EVALUACION PARA MARCAR EL ATRIBUTO quiz
                    if (line.equals("ESTADISTICA") || line.equals("EVALUACION")) {
                        if (line.equals("EVALUACION")) {
                            quiz.setEvaluation(true);
                        }
                    } else {
                        System.out.println("Error en archivo: Contenido:" + fileQuiz + " no indica si es ESTADISTICA o EVALUACION en su primera linea!");
                        break;
                    }
                } else {
                    if (lineid == 1) {
                        // SETEO EL TEMA DEL CUESTIONARIO
                        quiz.setTopic(line);
                    } else {
                        //SI HAY UNA PREGUNTA ANTERIOR LA AGREGO A LAS PREGUNTAS DE quiz.

                        String[] questParts = line.split(",");
                        if (questParts.length == 2) {
                            // PREGUNTA
                            if (q != null) {
                                // agrego la pregunta a quiz
                                quiz.addQuestion(q);

                            }
                            q = new Question();
                            q.setQuestion(questParts[1]);
                            AnswerNumber = 1;
                        } else if (questParts.length == 3) {
                            // RESPUESTA
                            if (q != null) {
                                Reply r = new Reply();
                                r.setNumber(AnswerNumber);
                                r.setOption(questParts[1]);
                                r.setRight(Boolean.parseBoolean(questParts[2]));
                                q.addOption(r);
                                AnswerNumber++;
                            }
                        }
                    }
                }
                lineid++;
            }
            if (q != null) {
                // agrego la pregunta a quiz
                quiz.addQuestion(q);

            }
            return  true;
        } catch (FileNotFoundException fnfe) {
            System.out.println("Error en archivo: Exepcion:"+fileQuiz + " no existe!");
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            System.out.println("Error en archivo: IOExepcion:"+fileQuiz + " no se puede leer!");
            ioe.printStackTrace();
            return false;
        }
    }

    private static void fillUser() throws IOException {
        User user = new User();
        String n;
        System.out.print("Ingrese su nombre: ");
        n = scanner.next();
        user.setName(n);
        String a;
        System.out.print("Ingrese su edad: ");
        a = scanner.next();
        user.setAge(Integer.parseInt(a));
        quiz.setUser(user);
        System.out.println("Respuestas de: " + quiz.getUserData());


    }

    private static boolean fillQuiz() {
        quiz.getQuestions().forEach((q) -> {
            String outQ = q.getOpciones();
            System.out.println("Pregunta:" + q.getQuestion());
            System.out.println(outQ);
            if (q.isMultipleReply() > 1) {
                // LAS RESPUESTAS MULTIPLES PUEDEN QUEDAR SIN RESPUESTAS
                System.out.print("Ingrese sus respuesta : ");
                String rep = scanner.next();
                if (!rep.equals("0")) {
                    String[] arrRep = rep.split(",");
                    for (String repl : arrRep) {
                        //Integer i = Integer.parseInt(repl);
                        q.addAnswer(Integer.parseInt(repl));
                    }
                } else {
                    System.out.println("PREGUNTA SIN RESPONDER. ");
                }
            } else {
                // CONTROLAR QUE SE RESPONDA SI O SI
                boolean ok = false;
                while (!ok) {
                    System.out.print("Ingrese sus respuesta (si son varias, separadas por comas): ");
                    int rep = scanner.nextInt();
                    if (rep >= 1 && rep <= q.getOptions().size()) {
                        q.addAnswer(rep);
                        ok = true;
                    } else {
                        System.out.println("LA RESPUESTA NO ES VALIDA, POR FAVOR REINGRESE. ");
                    }
                }
            }
        });

        return true;

    }

    private static void save() throws IOException {
        try {
            writerOutput = new FileWriter(fileOutput);

            String txt = (quiz.getEvaluation() ? "EVALUACION" : "ESTADISTICA") + "\n";
            txt += quiz.getTopic()+ "\n";
            txt += quiz.getUserDataForExport()+ "\n";
            final String[] text = {""};
            quiz.getQuestions().forEach((q) -> {
                text[0] += "P," + q.getQuestion() + "\n";
                final String[] asws = {""};
                q.getAnswers().forEach((r) -> asws[0] += "R," + r.getNumber() + "\n");
                //q.getAnswers().stream().forEach((r) -> { asws[0] += String.valueOf(r.getNumber()) +" ->" +  r.getOption() + "; ";  });
                String asws1 = Arrays.toString(asws).replace("[","").replace("]","");

                text[0] += asws1;
                if (asws1.equals("")) {
                    text[0] += "R,0\n";
                }
                /*
                if (quiz.getEvaluation()) {
                    // AGREGAR RENLGON CON MENSAJE SI LA RESPUESTA ES CORRECTA O NO
                    final boolean ok = true;
                    ArrayList<Reply> opcs = q.getOptions();
                    ArrayList<Reply> answ = q.getAnswers();
                    boolean[] resps = new boolean[opcs.size()];
                    for (int i = 0; i < opcs.size(); i++) {
                        boolean founded = false;
                        for (int z = 0; z < answ.size(); z++) {
                            if ((opcs.get(i).getNumber() == answ.get(z).getNumber() && opcs.get(i).getRight())) {
                                //esta la pregunta en las respuestas y debe estarlo
                                founded = true;
                                break;
                            }
                        }
                        if (!founded && !opcs.get(i).getRight()) {
                            //no se encontro pero es falsa. se cuenta como verdadera para no anular el resultado
                            founded = true;
                        }
                        resps[i] = founded;
                    }
                    boolean correcta = true;
                    for (int i = 0; i < opcs.size(); i++) {
                        if (!resps[i]) {
                            correcta = false;
                            break;
                        }
                    }
                    if (correcta) {
                        text[0] += "OK\n";
                    } else {
                        text[0] += "INCORRECTA\n";
                    }

                        //q.getOptions().stream().forEach((r) -> {
                     //Reply a = q.getAnswers().stream().filter((x) -> x.getNumber() == r.getNumber())
                      //              .collect(Collectors.toList()).get(0);

                    String xxx = "";
                }
                */



            });
            txt += Arrays.toString(text).replace("[","").replace("]","");
            writerOutput.write(txt);
            writerOutput.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println("Error en archivo: Exepcion:"+fileOutput + " no existe!");
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Error en archivo: IOExepcion:"+fileOutput + " no se puede escribir!");
            ioe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Error en archivo: Exepcion:"+fileOutput + " excepcion general!");
            e.printStackTrace();
        }
    }

    private static void evaluate() throws IOException {
        try {
            if (loadQuiz()) {
                File f = new File(evaluateDir);
                ArrayList<String> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.list())));
                if ( files.size() > 0) {
                    ArrayList<Quiz> qs = new ArrayList<>();
                    files.stream().filter((fi) -> fi.contains(evaluatePrefix))
                            .forEach((fi) -> {
                                //recorro los archivos que contienen el prefijo ingresado en el nombre
                                Quiz qq = new Quiz();
                                qs.add(qq);

                            });

                    File fq = new File(fileQuiz);
                    BufferedReader rdr = new BufferedReader(new FileReader(fq));
                    String line;
                    int lineid = 0;
                    int AnswerNumber = 1;
                    Question q = null;
                    while ((line = rdr.readLine()) != null) {
                        if (lineid == 0) {
                            // VEO SI ES ESTADISTICA O EVALUACION PARA MARCAR EL ATRIBUTO quiz
                            if (line.equals("ESTADISTICA") || line.equals("EVALUACION")) {
                                if (line.equals("EVALUACION")) {
                                    quiz.setEvaluation(true);
                                }
                            } else {
                                System.out.println("Error en archivo: Contenido:" + fileQuiz + " no indica si es ESTADISTICA o EVALUACION en su primera linea!");
                                break;
                            }
                        } else {
                            if (lineid == 1) {
                                // SETEO EL TEMA DEL CUESTIONARIO
                                quiz.setTopic(line);
                            } else {
                                //SI HAY UNA PREGUNTA ANTERIOR LA AGREGO A LAS PREGUNTAS DE quiz.

                                String[] questParts = line.split(",");
                                if (questParts.length == 2) {
                                    // PREGUNTA
                                    if (q != null) {
                                        // agrego la pregunta a quiz
                                        quiz.addQuestion(q);

                                    }
                                    q = new Question();
                                    q.setQuestion(questParts[1]);
                                    AnswerNumber = 1;
                                } else if (questParts.length == 3) {
                                    // RESPUESTA
                                    if (q != null) {
                                        Reply r = new Reply();
                                        r.setNumber(AnswerNumber);
                                        r.setOption(questParts[1]);
                                        r.setRight(Boolean.parseBoolean(questParts[2]));
                                        q.addOption(r);
                                        AnswerNumber++;
                                    }
                                }
                            }
                        }
                        lineid++;
                    }
                    if (q != null) {
                        // agrego la pregunta a quiz
                        quiz.addQuestion(q);
                     
                    }

                } else {
                    System.out.println("Error: no hay archivos que contengan " + evaluatePrefix +"." );
                }
            } else {
                System.out.println("Error: " + fileQuiz + " no se pudo cargar la encuesta/evaluacion!");
            }


        } catch (FileNotFoundException fnfe) {
            System.out.println("Error en archivo: Exepcion:"+fileOutput + " no existe!");
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Error en archivo: IOExepcion:"+fileOutput + " no se puede escribir!");
            ioe.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Error en evaluacion de respuestas: Excepcion general!");
            e.printStackTrace();
        }

    }








}


