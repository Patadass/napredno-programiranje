import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class TriviaQuestion {

    public String question;		// Actual question
    public String answer;		// Answer to question
    public int value;			// Point value of question

    public TriviaQuestion() {
        question = "";
        answer = "";
        value = 0;
    }

    public TriviaQuestion(String q, String a, int v) {
        question = q;
        answer = a;
        value = v;
    }

    public boolean checkAnswer(String ans){
        return false;
    }
}

class TrueFalseQuestion extends TriviaQuestion{
    public TrueFalseQuestion(){
        super();
    }
    public TrueFalseQuestion(String q, String a, int v){
        super(q,a,v);
    }

    @Override
    public boolean checkAnswer(String ans){
        return ans.charAt(0) == this.answer.charAt(0);
    }

    @Override
    public String toString(){
        return String.format("%s\nEnter 'T' for true or 'F' for false.", this.question);
    }
}

class FreeFormQuestion extends TriviaQuestion{
    public FreeFormQuestion(){
        super();
    }
    public FreeFormQuestion(String q, String a, int v){
        super(q,a,v);
    }

    @Override
    public boolean checkAnswer(String ans){
        return ans.toLowerCase().equals(this.answer.toLowerCase());
    }

    @Override
    public String toString(){
        return String.format("%s", this.question);
    }
}

class TriviaData {

    private ArrayList<TriviaQuestion> data;

    public TriviaData() {
        data = new ArrayList<TriviaQuestion>();
    }

    public void addQuestion(TriviaQuestion q) {
        data.add(q);
    }

    public void showQuestion(int index) {
        TriviaQuestion q = data.get(index);
        System.out.println("Question " + (index + 1) + ".  " + q.value + " points.");
        System.out.println(q);
    }

    public int numQuestions() {
        return data.size();
    }

    public TriviaQuestion getQuestion(int index) {
        return data.get(index);
    }
}

public class TriviaGame {

    public TriviaData questions;	// Questions

    public TriviaGame() {
        // Load questions
        questions = new TriviaData();
        questions.addQuestion(new FreeFormQuestion("The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3));
        questions.addQuestion(new TrueFalseQuestion("Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1));
        questions.addQuestion(new FreeFormQuestion("1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2));
        questions.addQuestion(new FreeFormQuestion("I am the geometric figure most like a lost parrot",
                "polygon", 2));
        questions.addQuestion(new TrueFalseQuestion("Generics were introducted to Java starting at version 5.0.",
                "T", 1));
    }
    // Main game loop

    public static void main(String[] args) {
        int score = 0;			// Overall score
        int questionNum = 0;	// Which question we're asking
        TriviaGame game = new TriviaGame();
        Scanner keyboard = new Scanner(System.in);
        // Ask a question as long as we haven't asked them all
        while (questionNum < game.questions.numQuestions()) {
            // Show question
            game.questions.showQuestion(questionNum);
            // Get answer
            String answer = keyboard.nextLine();
            // Validate answer
            TriviaQuestion q = game.questions.getQuestion(questionNum);
            if(q.checkAnswer(answer)){
                System.out.println("That is correct!  You get " + q.value + " points.");
                score += q.value;
            }else{
                System.out.println("Wrong, the correct answer is " + q.answer);
            }
            System.out.println("Your score is " + score);
            questionNum++;
        }
        System.out.println("Game over!  Thanks for playing!");
        keyboard.close();
    }
}
