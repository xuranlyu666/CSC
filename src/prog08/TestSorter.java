package prog08;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class TestSorter<E extends Comparable<E>> {

    public static void main (String[] args) {
	 tests(new InsertionSort<Integer>());
	 tests(new HeapSort<Integer>());
	 tests(new QuickSort<Integer>());
	 tests(new MergeSort<Integer>());
    }

    public static void tests (Sorter<Integer> sorter) {
	test(sorter, 10);
	test(sorter, 10000);
	test(sorter, 100000);
	test(sorter, 1000000);
	test(sorter, 1000000);
	test(sorter, 1000000);
	test(sorter, 10000000);

	System.out.println();
    }
  
    public static void test (Sorter<Integer> sorter, int n) {
	if (sorter instanceof InsertionSort && n > 100)
	    n /= 100;

	Integer[] array = new Integer[n];
	Random random = new Random(0);
	for (int i = 0; i < n; i++)
	    array[i] = random.nextInt(n);

	TestSorter<Integer> tester = new TestSorter<Integer>();
	tester.test(sorter, array);
    } // end of static test


	// Exercise
    public void test (Sorter<E> sorter, E[] array) {

	System.out.println(sorter + " on array of length " + array.length);

	if (inOrder(array))
	    System.out.println("array is already sorted!");

	E[] copy = array.clone();
	long time1 = System.nanoTime();
	sorter.sort(copy);
	long time2 = System.nanoTime();

	// EXERCISE
	// Print out number of MICROseconds and the constant.
		long durationInMicrosecond = ( time2 - time1 ) / 1000;
		double constant = sorter.O(array.length);

		System.out.println("Sorting took " + durationInMicrosecond + " microseconds.");
		System.out.println("Estimated time complexity constant: " + constant);


	if (!sameElements(array, copy))
	    System.out.println("sorted array does not have the same elements!");

	if (!inOrder(copy))
	    System.out.println("sorted array is not sorted");

	if (array.length < 100) {
	    print(array);
	    print(copy);
	}
    }  // end of test




    public void print (E[] array) {
	String s = "";
	for (E e : array)
	    s += e + " ";
	System.out.println(s);
    } // end of print

    /** Check if array is nondecreasing. */
    public boolean inOrder (E[] array) {
	// EXERCISE
		for(int i = 1; i < array.length - 1; i ++ ){
			if (array[i-1].compareTo(array[i]) > 0){
				return false;
			}
		}

	return true;
    }
 
    /* Check if arrays have the same elements. */
    public boolean sameElements (E[] array1, E[] array2) {
	// EXERCISE 1
	// If the two arrays have different lengths, return false.
		if (array1.length != array2.length){
			return false;
		}

	// EXERCISE 2
	// Create a Map from E to Integer, using the HashMap implementation.
		Map <E, Integer> map = new HashMap<E, Integer>();


	// EXERCISE 3
	// For each element of the first array, if it is not a key in the
	// map, make it map to 1.  If it is already a key, increment
	// the integer it maps to.
		for(int i = 0; i < array1.length; i ++){
			if (!map.containsKey(array1[i])){
				map.put(array1[i], 1);
			}else{
				map.put(array1[i], map.get(array1[i])+1);
			}

		} // end of for-loop


	// EXERCISE 4
	// For each element of the second array, if it is not a key in
	// the map, return false.  If it maps to zero, return false.
	// Otherwise, decrement the value that it maps to.
		for (int i = 0; i < array2.length; i ++){
			if (! map.containsKey(array2[i])){
				return false;
			}
			else if (map.get(array2[i]) == 0){
				return false;
			}
			else{
				map.put(array2[i], map.get(array2[i])-1);
			}
		} // end of for
	return true;
    } // end of same elements

} // end of TestSorter

class InsertionSort<E extends Comparable<E>> implements Sorter<E> {
    public double O (int n) { return 1; } // Use pow for this one.

    public void sort (E[] array) {

	for (int n = 0; n < array.length; n++) {
	    E data = array[n];
	    int i = n;

	    // EXERCISE
	    // while array[i-1] > data move array[i-1] to array[i] and
	    // decrement i
		while(i > 0 && array[i-1].compareTo(data)>0){
			array[i] = array [i-1];
			i--;
		}


	    array[i] = data;
	} // end of for

    } // end of inside sort

} // end of insertion sort

class HeapSort<E extends Comparable<E>>
    implements Sorter<E> {

    public double O (int n) { return 1; }

    private E[] array;
    private int size;

    public void sort (E[] array) {
	this.array = array;
	this.size = array.length;

	for (int i = parent(array.length - 1); i >= 0; i--)
	    swapDown(i);

	while (size > 1) {
	    swap(0, size-1);
	    size--;
	    swapDown(0);
	}
    } // end of sort (in HeapSort)

    public void swapDown (int index) {
	// EXERCISE

	// While the element at index is smaller than one of its children,
	// swap it with its larger child.  Use the helper methods provided
	// below: compare, swap, left, right, and isValid.
		while(true){

			int left = left(index);
			int right = right(index);
			int largerChild;

			if(!isValid(left)){
				break;
			}

			if (isValid(right) && compare(left, right)<0 ){
				largerChild = right;
			}
			else{
				largerChild = left;
			}

			if (compare(index, largerChild)<0 ){
				swap(index, largerChild);
				index = largerChild;
			}
			else{
				break;
			}

		}

    } // end of swap down

    // index = swap(index, left(index)) or
    // index = swap(index, right(index))
    private int swap (int i, int j) {
	E data = array[i];
	array[i] = array[j];
	array[j] = data;
	return j;
    } // end of swap

    private int compare (int i, int j) { return array[i].compareTo(array[j]); }
    private int left (int i) { return 2 * i + 1; }
    private int right (int i) { return 2 * i + 2; }
    private int parent (int i) { return (i - 1) / 2; }
    private boolean isValid (int i) {return 0 <= i && i < size;}

} // end of HeapSort





class QuickSort<E extends Comparable<E>>
    implements Sorter<E> {

    public double O (int n) { return 1; }

    private E[] array;

    private void swap (int i, int j) {
	E data = array[i];
	array[i] = array[j];
	array[j] = data;
    }

    public void sort (E[] array) {
	this.array = array;
	sort(0, array.length-1);
    }

    private void sort (int first, int last) {
	if (first >= last)
	    return;

	E pivot = array[(first + last) / 2];

	int lo = first;
	int hi = last;
	while (lo <= hi) {
	    // EXERCISE
	    // Move lo forward if array[lo] < pivot
		while (lo <= hi && array[lo].compareTo(pivot) < 0){
			lo++;
		}

	    // Otherwise move hi backward if array[hi] >= pivot
		while (lo <= hi && array[hi].compareTo(pivot) > 0){
			hi--;
		}



		// Otherwise swap array[lo] and array[hi] and move both lo and hi.
		if (lo <= hi){
			swap(lo, hi);
			lo++;
			hi--;
		}

	}

	for (int i = lo; i <= last; i++)
	    // EXERCISE
	    // If array[i] equals the pivot
		if (array[i].compareTo(pivot) == 0){
			swap(lo, i);
			lo++;
		}

		// Swap array[lo] and array[i] and increment first.


	sort(first, hi);
	sort(lo, last);
    } // end of sort

} // end of QuickSort

class MergeSort<E extends Comparable<E>>
    implements Sorter<E> {

    public double O (int n) { return 1; }

    private E[] array, extra;

    public void sort (E[] array) {
	this.array = array;
	extra = array.clone();
	sort(0, array.length-1);
    }

    private void sort(int first, int last) {
	if (first >= last)
	    return;

	int middle = (first + last) / 2;
	sort(first, middle);
	sort(middle+1, last);

	int in1 = first; // goes from first to middle in array
	int in2 = middle+1; // goes from middle+1 to last in array
	int out = first; // goes from first to last in extra (array)
	while (in1 <= middle && in2 <= last) {
	    // EXERCISE
	    // Copy the smaller of array[in1] or array[in2] to extra[out]
	    // (in the case of a tie, copy array[in1] to keep it stable)
	    // and increment out and in1 or in2 (the one you copied).
		if (array[in1].compareTo(array[in2]) <= 0){
			extra[out++] = array[in1++];
		}
		else{
			extra[out++] = array[in2++];
		}

	}

	// EXERCISE
	// Copy the rest of in1 or in2, whichever is not at the end.
		while(in1 <= middle){
			extra[out++] = array[in1++];
		}
		while (in2 <= last){
			extra[out++] = array[in2++];
		}

	// Move result from extra array back to original array.
	System.arraycopy(extra, first, array, first, last - first + 1);

    } // end of sort ( In MergeSort )

} // end of MergeSort
