package prog06;
import java.util.*;
import java.util.Map.Entry;

public class LinkedMap <K extends Comparable<K>, V>
		extends AbstractMap<K, V> {

	protected class Entry implements Map.Entry<K, V> {
		K key;
		V value;
		Entry previous, next;


		Entry (K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey () { return key; }

		public V getValue () { return value; }

		public V setValue (V newValue) {

			V oldValue = value;
			value = newValue;
			return oldValue;

		} // end of setter

		public String toString () {
			return "{" + key + "=" + value + "}";
		}
	} // End of Entry


	protected Entry first, last;




	/**
	 * Find the earliest Entry e with e.key ">=" key.
	 * @param key The Key to be found.
	 * @return The Entry e with e.key ">=" key
	 * or null if there isn't one.
	 */
	protected Entry find(K key) {
		// EXERCISE
		// Look at size() method.
		// Start at the first entry in the list.
		for (Entry entry = first; entry != null; entry = entry.next)
			// If the current entry's key is greater than or equal to the key we're looking for,
			// return the current entry.
			if (entry.key.compareTo(key) >= 0)
				return entry;
		// If we've gone through the list and haven't found an entry with a key
		// greater than or equal to the one we're looking for, return null.
		return null;
	}





	/**
	 * Determine if the Entry returned by find is the one we are looking
	 * for.
	 * @param entry The Entry returned by find.
	 * @param key The Key to be found.
	 * @return true if find found the entry with that key
	 * or false otherwise
	 */
	protected boolean found(Entry entry, K key) {
		// EXERCISE
		// Fix this.
		// Check if the entry is not null and the key in the entry matches the key we're looking for.
		return entry != null && entry.key.equals(key);
	}





	public boolean containsKey (Object keyAsObject) {
		K key = (K) keyAsObject;
		Entry entry = find(key);
		return found(entry, key);
	}



	public V get(Object keyAsObject) {
		// EXERCISE
		// Look at containsKey.
		K key = (K) keyAsObject;
		Entry entry = find(key);
		if (found(entry, key)) {
			// If Entry with key was found, return its value.
			return entry.getValue();
		}
		// If the entry was not found, return null.
		return null;
	}



	/**
	 * Add newEntry just before nextEntry or as last Entry if
	 * nextEntry is null.
	 * @param nextEntry Entry after newEntry or null if there isn't one.
	 * @param newEntry The new Entry to be inserted previous to nextEntry.
	 */
	protected void add(Entry nextEntry, Entry newEntry) {
		// EXERCISE

		if (first == null) {
			first = last = newEntry;
			return;
		}


		if (nextEntry == null) {
			last.next = newEntry;
			newEntry.previous = last;
			last = newEntry;
		} else {

			newEntry.next = nextEntry;
			newEntry.previous = nextEntry.previous;
			if (nextEntry.previous == null) {

				first = newEntry;
			} else {

				nextEntry.previous.next = newEntry;
			}
			nextEntry.previous = newEntry;
		}
	}





	public V put(K key, V value) {
		Entry entry = find(key);
		if (found(entry, key)) {
			// Handle the case that the key is already there.
			// Save yourself typing: setValue returns the old value!
			return entry.setValue(value);
		} else {
			// Key was not found, so we need to create a new entry.
			Entry newEntry = new Entry(key, value);
			// Add newEntry just before the entry that is larger,
			// or at the end if no such entry exists (entry is null).
			add(entry, newEntry);
			// Since this is a new key, we return null because there was no previous value.
			return null;
		}
	}


	/**
	 * Remove Entry entry from list.
	 * @param entry The entry to remove.
	 */
	protected void remove (Entry entry) {
		// EXERCISE
		///

		if (entry == null){
			// does not remove anything
			return;
		}

		if (entry == first){
			first = entry.next;
			if (first != null){
				first.previous = null;
			}
		}

		if(entry == last){
			last = entry.previous;
			if(last != null){
				last.next = null;
			}
		}

		if (entry.previous != null && entry.next != null){
			entry.previous.next = entry.next;
			entry.next.previous = entry.previous;
		}

		entry.previous = null;
		entry.next = null;







		///
	}


	public V remove (Object keyAsObject) {
		// EXERCISE
		// Use find, but make sure you got the right Entry!
		// If you do, then remove it and return its value.
		///

		K key = (K) keyAsObject;

		Entry foundEntry = find(key);

		if(foundEntry != null && foundEntry.key.equals(key)){

			remove(foundEntry);

			return foundEntry.value;
		}


		///
		return null;
	}




	protected class Iter implements Iterator<Map.Entry<K, V>> {
		private Entry current = first;

		public boolean hasNext() {

			return current != null;
		}

		public Map.Entry<K, V> next() {

			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Entry entryToReturn = current;
			current = current.next;
			return entryToReturn;
		}




		public void remove() {

			throw new UnsupportedOperationException();
		}



	} //Iter


	public int size () {
		int count = 0;
		for (Entry entry = first; entry != null; entry = entry.next)
			count++;
		return count;
	}

	protected class Setter extends AbstractSet<Map.Entry<K, V>> {
		public Iterator<Map.Entry<K, V>> iterator () {
			return new Iter();
		}

		public int size () { return LinkedMap.this.size(); }
	}

	public Set<Map.Entry<K, V>> entrySet () { return new Setter(); }

	static void test (Map<String, Integer> map) {
		if (false) {
			map.put("Victor", 50);
			map.put("Irina", 45);
			map.put("Lisa", 47);

			for (Map.Entry<String, Integer> pair : map.entrySet())
				System.out.println(pair.getKey() + " " + pair.getValue());

			System.out.println(map.put("Irina", 55));

			for (Map.Entry<String, Integer> pair : map.entrySet())
				System.out.println(pair.getKey() + " " + pair.getValue());

			System.out.println(map.remove("Irina"));
			System.out.println(map.remove("Irina"));
			System.out.println(map.get("Irina"));

			for (Map.Entry<String, Integer> pair : map.entrySet())
				System.out.println(pair.getKey() + " " + pair.getValue());
		}
		else {
			String[] keys = { "Vic", "Ira", "Sue", "Zoe", "Bob", "Ann", "Moe" };
			for (int i = 0; i < keys.length; i++) {
				System.out.print("put(" + keys[i] + ", " + i + ") = ");
				System.out.println(map.put(keys[i], i));
				System.out.println(map);
				System.out.print("put(" + keys[i] + ", " + -i + ") = ");
				System.out.println(map.put(keys[i], -i));
				System.out.println(map);
				System.out.print("get(" + keys[i] + ") = ");
				System.out.println(map.get(keys[i]));
				System.out.print("remove(" + keys[i] + ") = ");
				System.out.println(map.remove(keys[i]));
				System.out.println(map);
				System.out.print("get(" + keys[i] + ") = ");
				System.out.println(map.get(keys[i]));
				System.out.print("remove(" + keys[i] + ") = ");
				System.out.println(map.remove(keys[i]));
				System.out.println(map);
				System.out.print("put(" + keys[i] + ", " + i + ") = ");
				System.out.println(map.put(keys[i], i));
				System.out.println(map);
			}
			for (int i = keys.length; --i >= 0;) {
				System.out.print("remove(" + keys[i] + ") = ");
				System.out.println(map.remove(keys[i]));
				System.out.println(map);
				System.out.print("put(" + keys[i] + ", " + i + ") = ");
				System.out.println(map.put(keys[i], i));
				System.out.println(map);
			}
		}
	}

	public static void main (String[] args) {
		Map<String, Integer> map = new LinkedMap<String, Integer>();
		test(map);
	}
}