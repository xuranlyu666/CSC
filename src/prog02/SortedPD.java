package prog02;

import java.io.*;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author
 */
public class SortedPD extends ArrayBasedPD {

    protected DirectoryEntry remove(int index) {

        DirectoryEntry entry = this.theDirectory[index];

        int i = index + 1;

        while (i < this.size) {
            this.theDirectory[i - 1] = this.theDirectory[i];
            i++;
        }

        this.size--;

        return entry;
    }



    protected DirectoryEntry add(int index, DirectoryEntry newEntry) {

        if (this.size == this.theDirectory.length) {
            this.reallocate();
        }

        int i = this.size;

        while (i > index) {

            this.theDirectory[i] = this.theDirectory[i - 1];

            i--;
        }
        this.theDirectory[index] = newEntry;

        this.size++;
        return newEntry;
    }



    protected boolean found(int index, String name) {


        return index < this.size && this.theDirectory[index].getName().equals(name);


    }




    protected int find(String name) {

        int low = 0;

        int high = this.size - 1;

        int middle;

        while (low <= high) {

            middle = low + (high - low) / 2;

            if (this.theDirectory[middle] == null) {

                high = middle - 1;

                continue;

            }

            int comparisonResult = this.theDirectory[middle].getName().compareTo(name);

            if (comparisonResult < 0) {

                low = middle + 1;

            } else if (comparisonResult > 0) {

                high = middle - 1;

            } else {

                return middle;
            }


        }

        return low;

    }




}
