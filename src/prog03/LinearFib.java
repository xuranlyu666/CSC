package prog03;

public class LinearFib  extends Fib{
    // Fib is an abstract class


    @Override
    public double fib(int n) {


        //  initializes  two variables, a and b, which represent the two previous Fibonacci numbers
        // In each iteration of the loop, it calculates the next Fibonacci number by summing a and b.
        // It then updates a to be the value of b (the previous Fibonacci number) and b to be the new Fibonacci number.
        // After n iterations, it returns a,  which is the nth Fibonacci number.

        double a = 0.0, b = 1.0;

        for (int i = 0; i < n; i++) {

            double temp = b;

            b = a + b;

            a = temp;
        }
        return a;

    }






    @Override
    public double O(int n) {

        return n;

    }







}
