package prog03;
import prog02.UserInterface;
import prog02.GUI;

/**
 *
 * @author vjm
 */



public class Main {
  /** Use this variable to store the result of each call to fib. */
  public static double fibn;



  /** Determine the time, in microseconds, it takes to calculate the
   n'th Fibonacci number.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @return the time for the call to fib(n)
   */

  public static double time (Fib fib, int n) {
    //Get the current time in nanoseconds
    long start = System.nanoTime();

    //Calculate the n'th Fibonacci number.  Store the
    //result in fibn
    fibn = fib.fib(n);

    //Get the current time in nanoseconds
    long end = System.nanoTime(); // fix this

     //Return the difference between the end time and the

     //start time divided by 1000.0 to convert to microseconds
    return (end - start) / 1000.0; // fix this
  }

  /** Determine the average time in microseconds it takes to calculate
   the n'th Fibonacci number.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @param ncalls the number of calls to average over
   @return the average time per call
   */




  public static double averageTime (Fib fib, int n, int ncalls) {
    // Copy the contents of Main.time here.

    // Add a "for loop" line before the line with the call to fib.fib
    // to make that line run ncalls times.

    // Modify the return value to get the average.

//    return 0; // remove this line

    double totalTime = 0.0;

    for (int i = 0; i < ncalls;  i++)

      totalTime = totalTime + time(fib, n);

    return totalTime / ncalls;




  }

  /** Determine the time in microseconds it takes to to calculate the
   n'th Fibonacci number.  Average over enough calls for a total
   time of at least one second.
   @param fib an object that implements the Fib interface
   @param n the index of the Fibonacci number to calculate
   @return the time it it takes to compute the n'th Fibonacci number
   */



  public static double accurateTime (Fib fib, int n) {
    // Get the time in microseconds for one call.
    double t = time(fib, n);

    // If the time is more than a second, return it.

    if (t > 1000000){

      return t;

    };

    // Estimate the number of calls that would add up to one second.
    // Use   (int)(YOUR EXPESSION)   so you can save it into an int variable.
    int numcalls = (int)( 1000000/t ); // fix this


    // Get the average time using averageTime above and that many
    // calls and return it.
    return averageTime(fib, n, numcalls);
  }









  //private static UserInterface ui = new TestUI("Fibonacci experiments");
  private static UserInterface ui = new GUI("Fibonacci experiments");

  /** Get a non-negative integer from the using using ui.
   If the user enters a negative integer, like -2, say
   "-2 is negative...invalid" and ask again.
   If the user enters a non-integer, like abc, say
   "abc is not an integer" and ask again.
   If the user clicks cancel, return -1.
   @return the non-negative integer entered by the user or -1 for cancel.
   */



  static int getInteger() {
    while (true) {
      try {
        String s = ui.getInfo("Enter n");
        if (s == null) {
          return -1;
        }

        int n = Integer.parseInt(s);
        if (n < 0) {

          ui.sendMessage(s + " is negative...invalid. Please enter a non-negative integer.");
        } else {
          return n;
        }
      } catch (NumberFormatException e) {

        ui.sendMessage("That's not an integer. Please enter a non-negative integer.");
      }

    }

  }





  public static void doExperiments(Fib fib) {

    boolean firstTime = true;
    double estimatedTime = 0;

    while (true) {


      int n = getInteger();//Ask the user for an integer using getInteger



      if (n == -1) { //Quit if the user cancel

        return;

      }

       //get the estimated time from fib (unless this is the first time around the loop) and display the estimated time (including units)
      if (!firstTime) {

        estimatedTime = fib.estimateTime(n);
        ui.sendMessage("Estimated time for fib(" + n + "): " + estimatedTime + " microseconds");

      }

       //need to call accurateTime to get the actual time
      double actualTime = accurateTime(fib, n);


      fib.recordConstant(n, actualTime); //Record the resulting constant in fib


      if (!firstTime) {
        double percentageError = ((actualTime - estimatedTime) / actualTime) * 100;

        ui.sendMessage("n: " + n + ", fib(" + n + "): " + fibn +
                ", actual time: " + actualTime + " microseconds, " +
                "percentage error: " + String.format("%.2f", percentageError) + "%");

      } else {


        ui.sendMessage("n: " + n + ", fib(" + n + "): " + fibn);
        firstTime = false;

      }

    }

  }





  public static void doExperiments() {
    String[] commands = {
            "ExponentialFib",
            "LinearFib",
            "ConstantFib",
            "LogFib",
            "MysteryFib",
            "Exit"
    };

    while (true) {
      int commandIndex = ui.getCommand(commands);
      if (commandIndex == -1 || commands[commandIndex].equals("Exit")) {
        return; // Exit if the user selects "Exit" or closes the dialog
      }

      Fib fib = null;
      switch (commandIndex) {
        case 0: fib = new ExponentialFib();
        break;
        case 1: fib = new LinearFib();
        break;
        case 2: fib = new ConstantFib();
        break;
        case 3: fib = new LogFib();
        break;
        case 4: fib = new MysteryFib();
        break;
      }

      doExperiments(fib);
    }

  }




  static void labExperiments () {
    // Create (Exponential time) Fib object and test it.
    Fib efib = new ExponentialFib();
    System.out.println(efib);
    for (int i = 0; i < 11; i++)
      System.out.println(i + " " + efib.fib(i));


    // Determine running time for n1 = 20 and print it out.
    int n1 = 20;
    double time1 = averageTime(efib, n1, 1000);
    System.out.println("n1 " + n1 + " time1 " + time1 + " microseconds");

    // Calculate constant:  time = constant times O(n).
    double c = time1 / efib.O(n1);
    System.out.println("c " + c);


    // Estimate running time for n2=30.
    int n2 = 30;
    double time2est = c * efib.O(n2);
    System.out.println("n2 " + n2 + " estimated time " + time2est + " microseconds");

    // Calculate actual running time for n2=30.
    double time2 = averageTime(efib, n2, 100);
    System.out.println("n2 " + n2 + " actual time " + time2 + " microseconds");


    // Estimate how long ExponentialFib.fib(100) would take.
    int n3 = 100;

    double time3est = c * efib.O(n3);

    // 1 million * 60 sec * 60 minutes * 24h * 365.25 days

    double years = time3est / 1000000 / 3600 / 24 / 365.25;

    System.out.println("n3 "+ n3 + " estimated calculation time is " + years + " years.");


  }

  /**
   * @param args the command line arguments
   */





  public static void main (String[] args) {

    labExperiments();
    doExperiments();
  }

}

