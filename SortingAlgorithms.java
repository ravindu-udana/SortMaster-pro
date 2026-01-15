
import java.util.Arrays;

public class SortingAlgorithms {

    /**
     * Insertion Sort
     * Time Complexity: O(n^2)
     */
    public static void insertionSort(double[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            double key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

     /**
     * Merge Sort
     * Time Complexity: O(n log n)
     * Note: This implementation is recursive and works on the array.
     */
    public static void mergeSort(double[] arr) {
        if (arr.length <= 1) return;
        
        int mid = arr.length / 2;
        double[] left = Arrays.copyOfRange(arr, 0, mid);
        double[] right = Arrays.copyOfRange(arr, mid, arr.length);

        mergeSort(left);
        mergeSort(right);

        merge(arr, left, right);
    }

    private static void merge(double[] result, double[] left, double[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
    }
}
