package prog04;

import java.util.Stack;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import prog02.UserInterface;
import prog02.GUI;
import prog02.ConsoleUI;

public class Calculator {

  static final String OPERATORS = "()+-*/u^";
  static final int[] PRECEDENCE = { -1, -1, 1, 1, 2, 2, 3, 4 };

  boolean pushedOp;
  Stack<Character> operatorStack = new Stack<Character>();
  Stack<Double> numberStack = new Stack<Double>();
  UserInterface ui = new GUI("Calculator");

  Calculator (UserInterface ui) {

    this.ui = ui;

  }


  //Implement precedence(op):
  // Return the precedence value of op.  For example,
  //   precedence('/') returns 2.
  //  int precedence (char op)
  int precedence(char symbol) {

    if (symbol == '(' || symbol == ')') return -1;

    else if (symbol == '+' || symbol == '-') return 1;

    else if (symbol == '*' || symbol == '/') return 2;

    else if (symbol == 'u') return 3;

    else if (symbol == '^') return 4;

    return 0;
  }






  void emptyStacks () {
    while (!numberStack.empty())
      numberStack.pop();
    while (!operatorStack.empty())
      operatorStack.pop();
  }




  String numberStackToString () {
    String s = "numberStack: ";
    Stack<Double> helperStack = new Stack<Double>();
    // EXERCISE
    // Put every element of numberStack into helperStack

    // You will need to use a loop.  What kind?

    // What condition? When can you stop moving elements out of numberStack?


    // What method do you use to take an element out of numberStack?

    // What method do you use to put that element into helperStack?

    // Now put everything back, but also add each one to s:

    // s = s + " " + number;

    while (!numberStack.isEmpty()) {
      double temp = numberStack.pop();
      helperStack.push(temp);
    }

    while (!helperStack.isEmpty()) {
      double temp = helperStack.pop();
      s += " " + temp;
      numberStack.push(temp);
    }
    return s;




  }





  String operatorStackToString () {
    String s = "operatorStack: ";
    // EXERCISE

    Stack<Character> helperStack = new Stack<Character>();

    while (!operatorStack.isEmpty()) {

      char op = operatorStack.pop();
      helperStack.push(op);

    }

    while (!helperStack.isEmpty()) {
      char op = helperStack.pop();
      s += " " + op;

      operatorStack.push(op);


    }

    return s;
  }



  void displayStacks () {
    ui.sendMessage(numberStackToString() + "\n" +
                   operatorStackToString());
  }





  void doNumber (double x) {
    numberStack.push(x);

    pushedOp = false ;

    displayStacks();
  }

  void doOperator (char op) {
    //At the top of doOperator, if op is '-' and either
   // both stacks are empty or
    //the last push was an operator
    //set op to 'u' instead.

    if (op == '-' && (   (operatorStack.empty() && numberStack.empty()) || pushedOp)   ) {

      op = 'u';

    }

    processOperator(op);
    displayStacks();
  }

  double doEquals () {
    while (!operatorStack.empty())
      evaluateTopOperator();

    return numberStack.pop();
  }
    
  double evaluateOperator (double a, char op, double b) {
    switch (op) {

      case '+':
        return a + b;
      case '-':
        return a - b;
      case '*':
        return a * b;
      case '/':
        return a / b;
      case '^':
        return Math.pow(a,b);


      // EXERCISE

    }
    System.out.println("Unknown operator " + op);
    return 0;
  }

  void evaluateTopOperator () {
    char op = operatorStack.pop();

    if (op == 'u') {

      numberStack.push(-numberStack.pop());
      pushedOp = false;
    } else {

      double secondOperand = numberStack.pop();
      double firstOperand = numberStack.pop();

      numberStack.push(evaluateOperator(firstOperand, op, secondOperand));
    }

    displayStacks();
    if (op != 'u') {
      return;
    }


  }


  void processOperator (char op) {

    if (op == '(' || op == 'u') {

      operatorStack.push(op);

      pushedOp = true;

    } else if (op == ')') {


      while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {

        evaluateTopOperator();

      }

      if (!operatorStack.isEmpty()) operatorStack.pop();

    } else {

      while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(op)) {

        evaluateTopOperator();

      }
      operatorStack.push(op);

      pushedOp = true;

    }

  }





  static boolean checkTokens (UserInterface ui, Object[] tokens) {
      for (Object token : tokens)
        if (token instanceof Character &&
            OPERATORS.indexOf((Character) token) == -1) {
          ui.sendMessage(token + " is not a valid operator.");
          return false;
        }
      return true;
  }

  static void processExpressions (UserInterface ui, Calculator calculator) {
    while (true) {
      String line = ui.getInfo("Enter arithmetic expression or cancel.");
      if (line == null)
        return;
      Object[] tokens = Tokenizer.tokenize(line);
      if (!checkTokens(ui, tokens))
        continue;
      try {
        for (Object token : tokens)
          if (token instanceof Double)
            calculator.doNumber((Double) token);
          else          
            calculator.doOperator((Character) token);
        double result = calculator.doEquals();
        ui.sendMessage(line + " = " + result);
      } catch (Exception e) {
        ui.sendMessage("Bad expression.");
        calculator.emptyStacks();
      }
    }
  }

  public static void main (String[] args) {
    UserInterface ui = new ConsoleUI();
    Calculator calculator = new Calculator(ui);
    processExpressions(ui, calculator);
  }
}
