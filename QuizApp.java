import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class QuizAndAssessmentSystem extends Application {

    private ObservableList<Question> questions = FXCollections.observableArrayList();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Stage primaryStage;
    private Scene mainMenuScene, quizScene, adminScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Quiz and Assessment System");

        // Main Menu
        VBox mainMenu = new VBox(15);
        mainMenu.setPadding(new Insets(20));
        mainMenu.setStyle("-fx-alignment: center;");

        Label welcomeLabel = new Label("Welcome to Quiz and Assessment System");
        Button startQuizBtn = new Button("Start Quiz");
        Button adminBtn = new Button("Admin Panel");
        Button exitBtn = new Button("Exit");

        mainMenu.getChildren().addAll(welcomeLabel, startQuizBtn, adminBtn, exitBtn);
        mainMenuScene = new Scene(mainMenu, 400, 300);

        // Admin Panel
        GridPane adminPane = new GridPane();
        adminPane.setPadding(new Insets(20));
        adminPane.setVgap(10);
        adminPane.setHgap(10);

        Label qLabel = new Label("Question:");
        TextField qField = new TextField();

        Label optALabel = new Label("Option A:");
        TextField optAField = new TextField();

        Label optBLabel = new Label("Option B:");
        TextField optBField = new TextField();

        Label optCLabel = new Label("Option C:");
        TextField optCField = new TextField();

        Label optDLabel = new Label("Option D:");
        TextField optDField = new TextField();

        Label ansLabel = new Label("Correct Answer (A/B/C/D):");
        TextField ansField = new TextField();

        Button addQBtn = new Button("Add Question");
        Button backToMenuBtn = new Button("Back to Menu");

        adminPane.add(qLabel, 0, 0); adminPane.add(qField, 1, 0);
        adminPane.add(optALabel, 0, 1); adminPane.add(optAField, 1, 1);
        adminPane.add(optBLabel, 0, 2); adminPane.add(optBField, 1, 2);
        adminPane.add(optCLabel, 0, 3); adminPane.add(optCField, 1, 3);
        adminPane.add(optDLabel, 0, 4); adminPane.add(optDField, 1, 4);
        adminPane.add(ansLabel, 0, 5); adminPane.add(ansField, 1, 5);
        adminPane.add(addQBtn, 0, 6); adminPane.add(backToMenuBtn, 1, 6);

        adminScene = new Scene(adminPane, 500, 400);

        // Quiz Scene Layout
        VBox quizLayout = new VBox(15);
        quizLayout.setPadding(new Insets(20));
        Label questionLabel = new Label();
        ToggleGroup optionsGroup = new ToggleGroup();

        RadioButton optA = new RadioButton();
        optA.setToggleGroup(optionsGroup);
        RadioButton optB = new RadioButton();
        optB.setToggleGroup(optionsGroup);
        RadioButton optC = new RadioButton();
        optC.setToggleGroup(optionsGroup);
        RadioButton optD = new RadioButton();
        optD.setToggleGroup(optionsGroup);

        Button nextBtn = new Button("Next");
        quizLayout.getChildren().addAll(questionLabel, optA, optB, optC, optD, nextBtn);
        quizScene = new Scene(quizLayout, 400, 300);

        // Button Actions
        startQuizBtn.setOnAction(e -> {
            if (questions.isEmpty()) {
                showAlert("No Questions", "Please add questions in the Admin Panel first.");
            } else {
                currentQuestionIndex = 0;
                score = 0;
                loadQuestion(questionLabel, optA, optB, optC, optD);
                primaryStage.setScene(quizScene);
            }
        });

        nextBtn.setOnAction(e -> {
            RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
            if (selected == null) {
                showAlert("Select Option", "Please select an answer.");
                return;
            }

            String answer = selected.getText();
            Question currentQ = questions.get(currentQuestionIndex);
            if (answer.equalsIgnoreCase(currentQ.getCorrectAnswer())) {
                score++;
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                loadQuestion(questionLabel, optA, optB, optC, optD);
                optionsGroup.selectToggle(null);
            } else {
                showAlert("Quiz Finished", "Your Score: " + score + "/" + questions.size());
                primaryStage.setScene(mainMenuScene);
            }
        });

        adminBtn.setOnAction(e -> primaryStage.setScene(adminScene));

        addQBtn.setOnAction(e -> {
            if (qField.getText().isEmpty() || ansField.getText().isEmpty()) {
                showAlert("Missing Data", "Please fill all fields.");
                return;
            }
            Question q = new Question(
                qField.getText(),
                optAField.getText(),
                optBField.getText(),
                optCField.getText(),
                optDField.getText(),
                ansField.getText().toUpperCase()
            );
            questions.add(q);
            showAlert("Added", "Question added successfully!");
            qField.clear(); optAField.clear(); optBField.clear(); optCField.clear(); optDField.clear(); ansField.clear();
        });

        backToMenuBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        exitBtn.setOnAction(e -> stage.close());

        stage.setScene(mainMenuScene);
        stage.show();
    }

    private void loadQuestion(Label label, RadioButton a, RadioButton b, RadioButton c, RadioButton d) {
        Question q = questions.get(currentQuestionIndex);
        label.setText((currentQuestionIndex + 1) + ". " + q.getQuestion());
        a.setText(q.getOptionA());
        b.setText(q.getOptionB());
        c.setText(q.getOptionC());
        d.setText(q.getOptionD());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

// Helper class for questions
class Question {
    private String question, optionA, optionB, optionC, optionD, correctAnswer;

    public Question(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() {
        switch (correctAnswer) {
            case "A": return optionA;
            case "B": return optionB;
            case "C": return optionC;
            case "D": return optionD;
            default: return "";
        }
    }
}