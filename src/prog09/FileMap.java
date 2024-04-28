package prog09;
import java.util.*;
import java.io.*;

public class FileMap extends ArrayMap {
    static int fileCounter;

    static String newFileName () {
	return fileCounter++ + ".txt";
    }

    boolean verbose = false;

    int level;


	// Constructor
    FileMap (int CAPACITY, String fileName, int level) {
	super(CAPACITY, fileName);
	this.level = level;
    }

	// method newFile that returns an ArrayMap
    ArrayMap newFile (String fileName) {
	if (level == 1)
	    return new ArrayMap(entries.length, fileName);
	else
	    return new FileMap(entries.length, fileName, level-1);
    }

	// method getFile
    ArrayMap getFile (int index) {
	// System.out.println("getFile index " + index);
	ArrayMap child = newFile(entries[index].value);
	child.read();
	return child;
    }

    public int size () {
	int n = 0;
	for (int i = 0; i < size; ++i)
	    n += getFile(i).size();
	return n;
    }

    // Returns index of file that COULD contain the key.
    // Return -1 if none can because key is less than minimum key.
    int findFile (String key) {
	int index = find(key);
	if (found(index, key))
	    return index;
	else
	    return index - 1;
    }

    public boolean containsKey (Object keyAsObject) {
	String key = (String) keyAsObject;
	// Find the index of the file that (possibly) contains the key.
	int index = findFile(key);
	if (index < 0)
	    return false;
	// Get that file and check if it actually contains the key.
	return getFile(index).containsKey(key);
    }
  
    public String get (Object keyAsObject) {
	// EXERCISE
	///
		String key = (String) keyAsObject;

		int index = findFile(key);

		if (index < 0){
			return null;
		}

		//return null; // fix
		return getFile(index).get(key);
	///
    }

    // Split the file at index in half and put the right half into a
    // new file at index+1.
    void split (int index) {
	if (verbose) {
	    System.out.println();
	    System.out.println(this);
	    System.out.println("split(" + index + ")");
	}
	// Left half is at index.
	ArrayMap left = getFile(index);
	// Right half is new file.
	String rightName = newFileName();
	ArrayMap right = newFile(rightName);
	// Move more than half capacity to the right.
	while (left.size > entries.length / 2)
	    right.add(0, left.remove(left.size - 1));
	// Write out modified files.
	left.write();
	right.write();
	if (verbose) {
	    System.out.println(left);
	    System.out.println(right);
	}
	// Add at index+1 a new Entry whose key is the minimum key in
	// the file and whose value is the file name.
	add(index+1, new Entry(right.entries[0].key, rightName));
    }

    public String put (String key, String value) {
	String oldValue = null;
	int index = findFile(key);

	// EXERCISE
	///
	// If key="Aaron" and the current minimum is "Ann",
	// what will be the value of index?
	// In which index ArrayMap do we want to put "Aaron"?
	// Update the value of index.
		if (index == -1){
			index = 0;
		}


	// Get the file at index.
		ArrayMap fileAtIndex = getFile(index);

	// Put the key and new value into the file at index,
	// and save the old value in oldValue.
		oldValue = fileAtIndex.put(key, value);

	// Write out the modified file.
		fileAtIndex.write();

	// Set the FileMap key at index to the minimum key in the file.
		entries[index].key = fileAtIndex.entries[0].key;

	// If the file at index is at capacity,
		if (fileAtIndex.size == fileAtIndex.entries.length)
	// split it using split() (above).
			split(index);

	///

	return oldValue;
    }

    // Merge the file at index with the one at index+1
    // and remove the one at index+1





    void join (int index) {
	ArrayMap left = getFile(index);
	ArrayMap right = getFile(index+1);
	// EXERCISE
	///
	// Remove each element from right (from what index?)
	// and add it to left (at what index?)
	// How do you know when you are done?
		while(right.size > 0){
			Entry entryToMove = right.remove(0);
			left.put(entryToMove.getKey(), entryToMove.getValue());
		}


	// Remove the FileMap Entry at index+1.
		remove(index + 1);

	// Save the left and right file.
		left.write();

	///
    }




    public String remove (Object keyAsObject) {
	if (false)
	    return null;

	String key = (String) keyAsObject;
	int index = findFile(key);

	// EXERCISE
	// Set true to false on the first line of this method.
	//
	// If key="Aaron" and the current minimum is "Ann",
	// what will be the value of index?
	// What should remove do?
	///
		if (index < 0){
			return null;
		}

	String value = null;
	// Get the file at index.
		ArrayMap file = getFile(index);


	// Remove the key from the file at index.
	// Save the value in value.
		 value = file.remove(key);

	// Set the key at index to the minimum key in the file.

			entries[index].key = file.entries[0].key;



	// Write the file.
		file.write();

	// If the file is less than half capacity.

	    // If index is not the last index

		// Join the map at index to the one at index+1

		// If the map at index at or greater than capacity,
		// split it


	    // Else if the index is the last index

		// How do we call join?

		// Which file might be too large now?

		// What do we do if it is?

		if (file.size < entries.length / 2){
			if(index < size - 1){
				join(index);
				//file = getFile(index);
				if(getFile(index).size >= entries.length){
					split(index);
				}
			}
			else if (index == size-1 && size > 1) {
				join(index - 1);
				//file = getFile(index - 1);
				if (getFile(index-1).size >= entries.length){
					split(index - 1);
				}
			}
		} // end of if

	///

	return value;
    } // end of remove

    protected class Iter implements Iterator<Map.Entry<String, String>> {
	int index = 0;
	Iterator<Map.Entry<String, String>> iter;
    
	Iter () {
	    if (index < size)
		iter = getFile(index).entrySet().iterator();
	}

	public boolean hasNext () { 
	    return index < size && iter.hasNext();
	}
    
	public Map.Entry<String, String> next () {
	    if (!hasNext())
		throw new NoSuchElementException();
	    Map.Entry<String, String> entry = iter.next();
	    if (!iter.hasNext()) {
		index++;
		if (index < size)
		    iter = getFile(index).entrySet().iterator();
	    }
	    return entry;
	}
    
	public void remove () {
	    throw new UnsupportedOperationException();
	}
    }
  
    protected class Setter extends AbstractSet<Map.Entry<String, String>> {
	public Iterator<Map.Entry<String, String>> iterator () {
	    return new Iter();
	}
    
	public int size () { return FileMap.this.size(); }
    }
  
    // public Set<Map.Entry<String, String>> entrySet () { return new Setter(); }

    void putTest (String key, String value) {
	if (verbose)
	    System.out.println("Testing put(" + key + ", " + value + ")");
	String ret = put(key, value);
	System.out.println("put(" + key + ", " + value + ") = " + ret);
	print(); // System.out.println(this);
	if (!get(key).equals(value))
	    System.out.println("ERROR: get(" + key + ") = " + get(key));
    }

    void removeTest (String key) {
	String v = get(key);
	if (verbose)
	    System.out.println("Testing remove(" + key + ")");
	String value = remove(key);
	if (!v.equals(value))
	    System.out.print("ERROR: ");
	System.out.println("remove(" + key + ") = " + value);
	if (verbose)
	    System.out.println("Testing remove(" + key + ")");
	value = remove(key);
	if (value != null)
	    System.out.println("ERROR: remove(" + key + ") = " + value);
	print(); // System.out.println(this);
    }

    void print (String indent) {
	for (int i = 0; i < size; i++) {
	    System.out.println(indent + entries[i].key);
	    System.out.println(indent + entries[i].value);
	    ArrayMap file = getFile(i);
	    String indent2 = indent + "  ";
	    if (level == 1) {
		for (int j = 0; j < file.size; j++) {
		    System.out.println(indent2 + file.entries[j].key);
		    System.out.println(indent2 + file.entries[j].value);
		}
	    }
	    else
		((FileMap) file).print(indent2);
	}
    }

    void print () {
	print("");
    }

    void addEntries(List<Map.Entry<String, String>> list) {
	for (int i = 0; i < size; i++) {
	    ArrayMap file = getFile(i);
	    if (level > 1)
		((FileMap) file).addEntries(list);
	     else
		 for (int j = 0; j < file.size; j++)
		     list.add((Map.Entry<String, String>) file.entries[j]);
	}
    }

    public static void main (String[] args) {
	String fileName = newFileName();
	ArrayMap file = new ArrayMap(4, fileName);
	file.put("b", 1 + "");
	file.write();
	FileMap map = new FileMap(4, "filemap.txt", 1);
	map.add(0, new Entry("b", fileName));

	map.putTest("c", 2 + "");
	map.putTest("c", 7 + "");
	map.putTest("a", 0 + "");
	map.putTest("d", 3 + "");
	map.putTest("d", 7 + "");
	map.removeTest("a");
	map.putTest("e", 9 + "");
	map.putTest("f", 8 + "");
	map.removeTest("b");
	map.removeTest("e");
	map.putTest("e", 9 + "");
	map.putTest("b", 1 + "");
	map.removeTest("f");
	map.putTest("f", 3 + "");
	map.putTest("g", 4 + "");
	map.removeTest("d");
	map.putTest("d", 6 + "");
	map.putTest("h", 8 + "");
	map.removeTest("e");
    }
}

