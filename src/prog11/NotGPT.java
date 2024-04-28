package prog11;

import prog05.ArrayQueue;
import prog08.ExternalSort;
import prog08.TestExternalSort;
import prog09.BTree;
import prog10.HashTable;

import javax.swing.text.InternationalFormatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class NotGPT implements SearchEngine{

    // step 3.1
    // Put a HardDisk variable pageDisk inside NotGPT to store the information about web pages. Initialize pageDisk
    HardDisk pageDisk = new HardDisk();

    HardDisk wordDisk = new HardDisk();

    // step 3.2
    // Put a Map<String, String> variable indexOfURL into NotGPT. Initialize to a new prog09.BTree(100).
    Map<String, String> indexOfURL = new BTree(100);

    Map<String, Long> indexOfWord = new HashMap<>();


    // step 4:
    public Long indexPage(String url){
/*
		takes the String url of the web page as input and
		returns the index of its newly created page file.
			It gets the index of a new file from pageDisk, creates a new InfoFile,and stores it in pageDisk under that index.
			Then it tells the Map indexOfURL to map url to that index and returns the index.
 */
        Long index = pageDisk.newFile(); // generate the new index of the new file
        InfoFile newInfoFile = new InfoFile(url);
        pageDisk.put(index, newInfoFile); // Hard Disk is a Map? -YES TreeMap
        indexOfURL.put(url, index.toString()); // BTree

        System.out.println("indexing url " + url + " index " + indexOfURL.get(url)
        + " file " + newInfoFile);

        return index;
    }


    public Long indexWord(String word){
        if (false){
            return null;
        }

        Long index = wordDisk.newFile();
        InfoFile wordInfoFile = new InfoFile(word);
        wordDisk.put(index, wordInfoFile);
        indexOfWord.put(word, index);

        System.out.println("indexing word " + word + " index " + indexOfWord.get(word) + " file " + wordInfoFile);
        return index;
    }

    // Step 5
    @Override
    public void collect(Browser browser, List<String> startingURLs) { // need browser and List as TWO inputs

        System.out.println("starting pages " + startingURLs);

        // create a queue of page indices.
        Queue<Long> pageIndices = new ArrayQueue<>();

        //   For each starting URL,
            // check if it has been indexed already (how?). If not,
            // index it using indexPage
            // and put the new index into the queue.
        for (String startURL : startingURLs){
            if (!indexOfURL.containsKey(startURL)){
                pageIndices.offer(indexPage(startURL));
            }
        }

        // Do the following while the queue is not empty:
            // Dequeue a page index.
            // Load its URL into the browser.
            // If it loads,
                // get its list of URLs.
                // For each URL in that list
                    // that has not been indexed yet,
                        // index it using indexPage
                        // and add its index to the queue.
        while (!pageIndices.isEmpty()){

            System.out.println("queue " + pageIndices);

            Long pageIndex = pageIndices.poll();

            System.out.println("dequeued " + pageDisk.get(pageIndex));

            String page = pageDisk.get(pageIndex).data;

            List<String> gatheredURLs = new ArrayList<>();
            List<String> gatheredWords = new ArrayList<>();

            Set<String> setofWords = new TreeSet<>();
            Set<String> setofURLs = new TreeSet<>();

            if (browser.loadPage(page)){
                gatheredURLs = browser.getURLs();

                gatheredWords = browser.getWords();
            }

            System.out.println("urls " + gatheredURLs);

            for (String loadedURL : gatheredURLs){
                if (!indexOfURL.containsKey(loadedURL)){
                    pageIndices.offer(indexPage(loadedURL));
                }

                if (!setofURLs.contains(loadedURL)){
                    setofURLs.add(loadedURL);
                    pageDisk.get(pageIndex).indices.add(Long.parseLong(indexOfURL.get(loadedURL)));
                }
            } // end of for

            System.out.println("updated page file " + pageDisk.get(pageIndex));
            System.out.println("words " + gatheredWords);

            for (String loadedWord : gatheredWords){
                if (indexOfWord.get(loadedWord) == null){
                    indexWord(loadedWord);
                }

                if (!setofWords.contains(loadedWord)){
                    setofWords.add(loadedWord);
                    wordDisk.get(indexOfWord.get(loadedWord)).indices.add(pageIndex);
                    System.out.println("updated word " + loadedWord + " index " + indexOfWord.get(loadedWord) +
                            " file " + wordDisk.get(indexOfWord.get(loadedWord)));
                }
            }
        }

    } // end of collect
    void rankSlow (){

        // hw:  initialize a double zeroLinkImpact zero
        double zeroLinkImpact = 0;
        int pageCount = 0;
        // visit each page file.  Inside the loop:
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            // hw
            if (file.indices.size() == 0){
                zeroLinkImpact += file.impact;
            }
            // Declare and initialize impactPerIndex, the amount of impact it will send to each index on that page.  (To what value?)
            //double impactPerIndex = file.impact/file.indices.size();
            // hw
            double impactPerIndex = file.impact/(file.indices.isEmpty() ? 1 : file.indices.size());

            // For each index on that page,
                // add impactPerIndex to the impactTemp of the page with that index.
            for (Long long0 : file.indices ){
                pageDisk.get(long0).impactTemp += impactPerIndex;
            }
            // hw
            pageCount ++;
        } // end of 1st for

        //hw
        zeroLinkImpact /= pageCount;

        //   Visit each page file again:
        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();

            //     Set its impact to its impactTemp.
            //     Set its impactTemp to 0.0.
            //file.impact = file.impactTemp;
            file.impact = file.impactTemp + zeroLinkImpact;
            file.impactTemp = 0.0D;
        } // end of 2nd for

    } // end of rankSlow



    void rankFast (){
        // copy in the code that sets zeroLinkImpact.
        double zeroLinkImpact = 0;
        int pageCount = 0;
        for(Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()){
            InfoFile file = entry.getValue();
            pageCount++;
            if (file.indices.isEmpty()){
                zeroLinkImpact += file.impact;
            }
            //pageCount++;
        }
        zeroLinkImpact /= pageCount;
        //In a try-catch, open a PrintWriter out on "unsorted-votes.txt".
        //
        //   Instead of adding impactPerIndex to impactTemp, create a vote with
        //   that impact.  Should the index of the Vote be teh votER or the
        //   votEE?
        //
        //   println that Vote to out.
        //
        //   After the loop, out.close();
        try{
            PrintWriter out = new PrintWriter("unsorted-votes.txt");
            for (Map.Entry<Long, InfoFile> entry : pageDisk.entrySet()){
                Long pageIndex = entry.getKey();
                InfoFile file = entry.getValue();
                double impactPerIndex = file.impact / (file.indices.isEmpty() ? 1 : file.indices.size());
                for (Long targetIndex : file.indices){
                    Vote vote = new Vote(targetIndex, impactPerIndex);
                    out.println(vote);
                }
            }
            out.close();
        //  Create a new VoteScanner.
        //   Create a new prog08.ExternalSort.
        //  Use it to sort "unsorted-votes.txt" into "sorted-votes.txt".
            ExternalSort<Vote> sorter = new ExternalSort<>(new VoteScanner());
            sorter.sort("unsorted-votes.txt", "sorted-votes.txt");
        }catch (FileNotFoundException e){
            System.out.println("File not found: " +e.getMessage());
        }
        //   After the try-catch, create another VoteScanner and call its
        //   iterator to get an iterator over "sorted-Votes.txt".
        VoteScanner voteScanner = new VoteScanner();
        Iterator<Vote> votes =  voteScanner.iterator("sorted-votes.txt");
        HashMap<Long, Double> tempImpacts = new HashMap<>();
        while(votes.hasNext()){
            Vote vote = votes.next();
            tempImpacts.putIfAbsent(vote.index, 0.0);
            tempImpacts.computeIfPresent(vote.index, (k, v) -> v + vote.vote);
        }
        for (Map.Entry<Long, Double> entry : tempImpacts.entrySet()){
            InfoFile file = pageDisk.get(entry.getKey());
            file.impact = entry.getValue() + zeroLinkImpact;
        }
    } // end of rankFast


    @Override
    public void rank(boolean fast) {

        for (Map.Entry<Long,InfoFile> entry : pageDisk.entrySet()) {
            long index = entry.getKey();
            InfoFile file = entry.getValue();
            // set the impact in each file
            //   to 1.0
            file.impact = 1.0D;
            //   and the impactTemp to 0.0.

            file.impactTemp = 0.0D;
        }
        //   If fast is false,
            // call rankSlow() 20 times.
        if (fast == false){
            for (int i = 0; i < 20; i ++){
                rankSlow();
            }
        }
        //   If fast is true,
            // call rankFast() 20 times.
        if (fast == true){
            for (int i = 0; i < 20; i ++){
                rankFast();
            }
        }
    } // end of method rank

    // 6. Add a Vote class to NotGPT with a Long index and a double vote.
    //
    //   Make it implement Comparable<Vote>.
    //
    //   The compareTo method should return the result of comparing the
    //   indexes if they are unequal.  Otherwise, it should return the
    //   result of comparing the impacts.  (Double.compare may be
    //   helpful.)
    //
    //   Also give it a public toString method that returns the index and
    //   impact separated by a space.
    public class Vote implements Comparable<Vote>{
        Long index;
        double vote;
        // constructor
        public Vote(Long index, double vote){
            this.index = index;
            this.vote = vote;
        }
        @Override
        public int compareTo(Vote o) {
            int indexComparison = index.compareTo(o.index);
            if (indexComparison != 0){
                return indexComparison;
            }
            return Double.compare(vote, o.vote);
            //return 0;
        }
        @Override
        public String toString(){
            return index + " " + vote;
        }
    } // end of Vote


    // 7. In order to use prog08.ExternalSort to soft votes, we need a
    //   VoteScanner class.  Copy the StringScanner class out of
    //   prog08/TestExternalSort.
    // Change String to Vote.
    //
    //   Don't make it static.
    //
    //   hasNext() should return in.hasNext()
    //
    //   next() should read the next Long and the next doulbe fro in, create
    //   a new Vote, and return it.

    class VoteScanner implements ExternalSort.EScanner<Vote> {
        class Iter implements Iterator<Vote> {
            Scanner in;
            Iter (String fileName) {
                try {
                    in = new Scanner(new File(fileName));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            public boolean hasNext () {
                return in.hasNext();
            }
            public Vote next () {
                //return in.nextLine();
                Long index = in.nextLong();
                double impact = in.nextDouble();
                return new Vote(index, impact);
            }
        }

        @Override
        public Iterator<Vote> iterator (String fileName) {
            return new Iter(fileName);
        }
    }


    @Override
    public String[] search(List<String> searchWords, int numResults) {

        int searchWordSize = searchWords.size();

        // step 1
        // create two variables
        // Iterator into list of page indices for each key word.
        Iterator<Long>[] pageIndexIterators = (Iterator<Long>[]) new Iterator[searchWordSize];

        // Current page index in each list, just ``behind'' the iterator.
        // initialize an array of Long of size
        long[] currentPageIndices;

        //   Write a loop to initialize the entries of pageIndexIterators.
            //   pageIndexIterators[i] should be set to an iterator over the page indices in the file of searchWords[i].

        for (int i = 0; i < searchWords.size(); i++){
            pageIndexIterators[i] = wordDisk.get((indexOfWord.get(searchWords.get(i)))).indices.iterator();
        }
        //   Initialize currentPageIndices.
            //   You just have to allocate the array.
            // You don't have to write a loop to initialize the elements
                //   of the array because all its elements will automatically be zero.
        currentPageIndices = new long[searchWordSize];
        // step 4
        // Implement the loop of search.  While getNextPageIndices is true
            //   check if the entries of currentPageIndices are all equal.
                //   If so, you have a found a match.  Print out its URL.
        ///
            while(getNextPageIndices(currentPageIndices, pageIndexIterators)){
                if (allEqual(currentPageIndices)){
                    System.out.println(pageDisk.get(currentPageIndices[0]).data);
                }
            }
        ///

        return new String[0];
    }



    // step 2
    /** Check if all elements in an array of long are equal.
     @param array an array of numbers
     @return true if all are equal, false otherwise
     */
    private boolean allEqual (long[] array) {

        for (int i = 0; i < array.length-1; i++){
            if (array[i] != array[i + 1]){
                return false;
            }
        }
        return true;
    }


    // step 2.1
    /** Get the largest element of an array of long.
     @param array an array of numbers
     @return largest element
     */
    private long getLargest (long[] array) {
        long largest = array[0];

        for (int i = 0; i < array.length; i++){
            if (array[i] > largest){
                largest = array[i];
            }
        }
        return largest;
    }





    // step 3
    //Implement getNextPageIndices
    /** If all the elements of currentPageIndices are equal,
     set each one to the next() of its Iterator,
     but if any Iterator hasNext() is false, just return false.

     Otherwise, do that for every element not equal to the largest element.

     Return true.

     @param currentPageIndices array of current page indices
     @param pageIndexIterators array of iterators with next page indices
     @return true if all page indices are updated, false otherwise
     */
    private boolean getNextPageIndices(long[] currentPageIndices, Iterator<Long>[] pageIndexIterators) {

        if (allEqual(currentPageIndices)){
            for (int i = 0; i < currentPageIndices.length; i++){
                if (pageIndexIterators[i].hasNext()){
                    currentPageIndices[i] = pageIndexIterators[i].next();
                }
                else{
                    return false;
                }
            }
        }
        else{
            long largestElement = getLargest(currentPageIndices);

            for (int i = 0; i < currentPageIndices.length; i++){
                if (currentPageIndices[i] != largestElement && pageIndexIterators[i].hasNext()){
                    currentPageIndices[i] = pageIndexIterators[i].next();
                }
                else if(currentPageIndices[i] != largestElement && !pageIndexIterators[i].hasNext()){
                    return false;
                }
            }
        }
        return true;
    }

    } // end of notGPT
