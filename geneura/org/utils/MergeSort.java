package geneura.org.utils;

import geneura.org.config.Configuration;
import geneura.org.individuals.Individual;
import geneura.org.individuals.binary.BinaryIndividual;
import geneura.samples.onemax.Onemax;



public class MergeSort {

    private Individual[] list;
    
    /**
     * Construct a new MergeSort object that will
     * sort the specified array of integers.
     *
     * @param listToSort the array of integers to be sorted.
     */
    public MergeSort(Individual[] listToSort) {
	list = listToSort;
    }

    /**
     * Get a reference to the array of integers in this
     * MergeSort object.
     *
     * @return a reference to the array of integers.
     */
    public Individual[] getList() {
	return list;
    }
    
    public Individual[] getFirstList(int pos) {
    	Individual toreturn[] = new Individual[pos];
    	for(int i=0;i<pos;i++){
    		toreturn[i]=(Individual)list[i].clone();
    	}
    	
    	return toreturn;
    }

    /**
     * Sort the values in the array of integers in this
     * MergeSort object into non-decreasing order.
     */
    public void sort() {
	list = sort(list);
    }
    
    /**
     * Recursive helper method which sorts the array referred to 
     * by whole using the merge sort algorithm.
     *
     * @param whole the array to be sorted.
     * @return a reference to an array that holds the elements
     *         of whole sorted into non-decreasing order.
     */
    private Individual[] sort(Individual[] whole) {
	if (whole.length == 1) {
	    return whole;
	}
	else {
	    // Create an array to hold the left half of the whole array
	    // and copy the left half of whole into the new array.
	    Individual[] left = new Individual[whole.length/2];
	    for(int i=0;i<left.length;i++){
	    	left[i] = (Individual)whole[i].clone();
	    }
	    

	    // Create an array to hold the right half of the whole array
	    // and copy the right half of whole into the new array.
	    Individual[] right = new Individual[whole.length-left.length];
	    for(int i=left.length;i<whole.length;i++){
	    	right[i-left.length] = (Individual)whole[i].clone();
	    }
	    // Sort the left and right halves of the array.
	    left = sort(left);
	    right = sort(right);

	    // Merge the results back together.
	    merge(left, right, whole);

	    return whole;
	}
    }

    /**
     * Merge the two sorted arrays left and right into the
     * array whole.
     *
     * @param left a sorted array.
     * @param right a sorted array.
     * @param whole the array to hold the merged left and right arrays.
     */
    private void merge(Individual[] left, Individual[] right, Individual[] whole) {
	int leftIndex = 0;
	int rightIndex = 0;
	int wholeIndex = 0;

	// As long as neither the left nor the right array has
	// been used up, keep taking the smaller of left[leftIndex]
	// or right[rightIndex] and adding it at both[bothIndex].
	while (leftIndex < left.length &&
	       rightIndex < right.length) {
	    if (left[leftIndex].better(right[rightIndex])) {
		whole[wholeIndex] = (Individual)left[leftIndex].clone();
		leftIndex++;
	    }
	    else {
		whole[wholeIndex] = (Individual)right[rightIndex].clone();
		rightIndex++;
	    }
	    wholeIndex++;
	}
	
	Individual[] rest;
	int restIndex;
	if (leftIndex >= left.length) {
	    // The left array has been use up...
	    rest = right;
	    restIndex = rightIndex;
	}
	else {
	    // The right array has been used up...
	    rest = left;
	    restIndex = leftIndex;
	}

	// Copy the rest of whichever array (left or right) was
	// not used up.
	for (int i=restIndex; i<rest.length; i++) {
	    whole[wholeIndex] = rest[i];
	    wholeIndex++;
	}
    }

    public static void main(String[] args) {

	BinaryIndividual[] arrayToSort = new BinaryIndividual[30];

	Onemax one = new Onemax();
	Configuration.minimization=false;
	Configuration.chromosome_size=10;
	for (int i=0;i<arrayToSort.length;i++){
		arrayToSort[i] = new BinaryIndividual(10);
		one.evaluate(arrayToSort[i]);
	}
	
	System.out.println("Unsorted:");
	for (int i=0;i<arrayToSort.length;i++){
		System.out.print(arrayToSort[i].getFitness()+" ");
		
	}
	System.out.println();	
	
	MergeSort sortObj = new MergeSort(arrayToSort);
	sortObj.sort();

	System.out.println("Sorted:");
	for (int i=0;i<arrayToSort.length;i++){
		System.out.print(sortObj.getList()[i].getFitness()+" ");
	}
	System.out.println();
	
	
    }
}


