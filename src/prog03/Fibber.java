package prog03;

/**
 *
 * @author vjm
 */
public class Fibber extends Fib {
  /** The Fibonacci number generator 0, 1, 1, 2, 3, 5, ...
      @param n index
      @return nth Fibonacci number
  */
    public double fib (int n) { return 1.0; }

  /** The order O() of the implementation.
      @param n index
      @return the function of n inside the O()
  */
    public double O (int n) { return n * n; }

    public static void main (String[] args) {
	Fib fib = new Fibber();
	fib.recordConstant(2, 8.0);
	double t = fib.estimateTime(4);
	if (t == 32)
	    System.out.println("correct");
	else
	    System.out.println("incorrect " + t);

	Fib fib2 = new MysteryFib();
	fib2.recordConstant(2, 8.0);
	t = fib2.estimateTime(4);
	if (t == 16)
	    System.out.println("MysteryFib is correct");
	else
	    System.out.println("MysteryFib is incorrect " + t);
    }
}
