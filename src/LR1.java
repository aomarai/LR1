import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class LR1 {

    private static String userExpression, currentToken;
    private static Stack<String> symbolStack = new Stack<>();
    private static Stack<Integer> stateStack = new Stack<>();
    private static String[] tempExpressionAsTokens;
    private static ArrayList<String> expressionAsTokens;

    //Splits the expression into tokens in a list and adds $ to end
    private static void tokenSplitter(){
        tempExpressionAsTokens = userExpression.split("((?<=\\+))|((?=\\+))|((?<=-))|((?=-))|((?<=\\*))|((?=\\*))|((?<=/))|((?=/))|((?<=\\())|((?=\\())|((?<=\\)))|((?=\\)))|((?<=\\$))|((?=\\$))");
        //Create new list from array and add $ to end of list
        expressionAsTokens = new ArrayList<>(Arrays.asList(tempExpressionAsTokens));
        expressionAsTokens.add(expressionAsTokens.size(),"$");
    }

    //Push the symbol to the stack, the state to the stack, then remove the symbol from the input queue
    private static void shift(int state){
        symbolStack.push(expressionAsTokens.get(0));
        stateStack.push(state);
        expressionAsTokens.remove(0);
        next();
    }

    private static void next(){
        currentToken = expressionAsTokens.get(0);

        switch (currentToken) {
            case "+": {}
            case "-": {}
            case "*": {}
            case "/": {}
            case "(": {}
            case ")": {}
            case "$": {}
            default: {
                try{
                    if (Integer.parseInt(currentToken) < 32767)
                        currentToken = "n";
                }catch (NumberFormatException e){
                    invalid();

                }

            }
        }
    }

    //Scan the expression and turn all integers into n
    private static void input(){
        for (int i = 0; i < expressionAsTokens.size(); i++){
            if (isOperand()){
                currentToken = "n";
            }
        }
    }

    //Tests to see if next variable in inputQueue is a number
    private static boolean isOperand(){
        return expressionAsTokens.get(0).matches("[0-9]+");
    }

    private static void invalid(){
        //System.out.println("The expression is invalid!");
       // System.exit(1);
    }

    //Actually parses the expression through the parsing table
    private static void parser(){
        symbolStack.push("-");
        stateStack.push(0);
        //Parser won't continue if this int is 0
        int parse = 1;
        //Makes sure the program should still run
        while (parse==1){
            int state = stateStack.peek();
            //expFront = expTokens.get(0);
            //Switch statement to begin actual parsing
            switch (currentToken){
                //If the first token in the remaining expression is n, shift to 5 from these states
                case "n":{
                    if (state == 0 || state == 4 || state == 6 || state == 7){
                        shift(5);
                    }
                    else {
                        invalid();
                        break;
                    }
                }
                case "+":{
                    //Just goes down the column of the parsing table looking for what to do
                    if (state==1 || state == 8)
                        shift(6);
                    else if (state == 2)
                        reduce(3);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 9){
                        //TODO: Implement choosing between E+T or E-T  for state 9
                        reduce(1);
                    }
                    else if (state == 10){
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;


                }
                case "-": {
                    if (state==1 || state == 8)
                        shift(6);
                    else if (state == 2)
                        reduce(3);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 9){
                        //TODO: Implement choosing between E+T or E-T  for state 9
                        reduce(1);
                    }
                    else if (state == 10){
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;
                }
                case "*":{
                    if (state == 2 || state == 9)
                        shift(7);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 10){
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;

                }
                case "/":{
                    if (state == 2 || state == 9)
                        shift(7);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 10){
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;
                }
                case "(":{
                    if (state == 0 || state == 4 || state == 6 || state == 7)
                        shift(4);
                    else invalid();
                    break;
                }
                case ")":{
                    if (state == 8)
                        shift(11);
                    else if (state == 2)
                        reduce(3);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 9){
                        //TODO: Implement choosing between E+T or E-T  for state 9
                        reduce(1);
                    }
                    else if (state == 10) {
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;
                }
                case "$":{
                    if (state == 1){
                        System.out.println("Valid Expression");
                        parse = 0;
                    }
                    else if (state == 2)
                        reduce(3);
                    else if (state == 3)
                        reduce(6);
                    else if (state == 5)
                        reduce(8);
                    else if (state == 9){
                        //TODO: Implement choosing between E+T or E-T  for state 9
                        reduce(1);
                    }
                    else if (state == 10){
                        //TODO: Implement choosing between T*F or T/F for state 10
                        reduce(4);
                    }
                    else if (state == 11)
                        reduce(7);
                    else invalid();
                    break;
                }
            }
            if (parse != 0){
                //Skips the first element in the stack because it is always 0
                for (int i = 1; i < symbolStack.size(); i++){
                    System.out.print("[(" + symbolStack.elementAt(i) + ":" + stateStack.elementAt(i) + ")]");
                }
                System.out.print("\t Input Queue: " + expressionAsTokens + "\n");

            }
        }

    }

    //Basically one giant switch statement that handles the reductions based on state
    private static void reduce(int rule){
        switch (rule){
            case 1: {
                //Reduce E -> E+T
                symbolStack.pop();
                symbolStack.pop();
                stateStack.pop();
                stateStack.pop();

                if (stateStack.peek() == 0){
                    symbolStack.push("E");
                    stateStack.push(1);
                }
                else if (stateStack.peek()==4){
                    symbolStack.push("E");
                    stateStack.push(8);
                }
                break;
            }
            case 2: {
                //Reduce E -> E-T
                symbolStack.pop();
                symbolStack.pop();
                stateStack.pop();
                stateStack.pop();

                if (stateStack.peek() == 0){
                    symbolStack.push("E");
                    stateStack.push(1);
                }
                else if (stateStack.peek()==4){
                    symbolStack.push("E");
                    stateStack.push(8);
                }
                break;
            }
            case 3: {
                //Reduce E -> T
                symbolStack.pop();
                stateStack.pop();

                if (stateStack.peek() == 0){
                    symbolStack.push("E");
                    stateStack.push(1);
                }
                else if (stateStack.peek() == 4){
                    symbolStack.push("E");
                    stateStack.push(8);
                }
                break;
            }
            case 4: {
                //Reduce T -> T*F
                symbolStack.pop();
                symbolStack.pop();
                stateStack.pop();
                stateStack.pop();
                if (stateStack.peek()== 0 || stateStack.peek() == 4){
                    symbolStack.push("T");
                    stateStack.push(2);
                }
                else if (stateStack.peek() == 6){
                    symbolStack.push("T");
                    stateStack.push(9);
                }
                break;
            }
            case 5: {
                //Reduce T-> T/F
                symbolStack.pop();
                symbolStack.pop();
                stateStack.pop();
                stateStack.pop();
                if (stateStack.peek()== 0 || stateStack.peek() == 4){
                    symbolStack.push("T");
                    stateStack.push(2);
                }
                else if (stateStack.peek() == 6){
                    symbolStack.push("T");
                    stateStack.push(9);
                }
                break;
            }
            case 6: {
                //Reduce T -> F
                symbolStack.pop();
                stateStack.pop();
                if (stateStack.peek() == 0 || stateStack.peek() == 4){
                    symbolStack.push("T");
                    stateStack.push(2);
                }
                else if (stateStack.peek() == 6){
                    symbolStack.push("T");
                    stateStack.push(9);
                }
                break;

            }
            case 7: {
                //Reduce F -> (E)
                symbolStack.pop();
                symbolStack.pop();
                symbolStack.pop();
                stateStack.pop();
                stateStack.pop();
                stateStack.pop();
                if (stateStack.peek() == 0 || stateStack.peek() == 4 || stateStack.peek() == 6){
                    symbolStack.push("F");
                    stateStack.push(3);
                }
                else if (stateStack.peek() == 7){
                    symbolStack.push("T");
                    stateStack.push(10);
                }
                break;

            }
            case 8: {
                //Reduce F -> n
                symbolStack.pop();
                stateStack.pop();
                if (stateStack.peek() == 0 || stateStack.peek() == 4 | stateStack.peek() == 6){
                    symbolStack.push("F");
                    stateStack.push(3);
                }
                else if (stateStack.peek() == 7){
                    symbolStack.push("F");
                    stateStack.push(10);
                }
            }
        }

    }

    public static void main(String[] args){
        //userExpression = args[0];
        userExpression = "57+5";

        symbolStack.push("-");
        stateStack.push(0);

        tokenSplitter();
        next();
        parser();


        System.out.println("Expression remaining as tokens: " + expressionAsTokens);
        System.out.println("Symbol stack: " + symbolStack);
        System.out.println("State stack: " + stateStack);
        System.out.println("Current Token: " + currentToken);

    }
}
