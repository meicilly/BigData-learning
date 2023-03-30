package 排序;

import java.util.Arrays;

/**
 * 冒泡排序
 * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。
 * 对每一对相邻元素做同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。
 * 针对所有的元素重复以上的步骤，除了最后一个。
 * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
 */
public class bubbleSort {
    public static void main(String[] args) {
        int[] array = {1, 3, 42, 5, 9};
        bubbleSort(array);
    }
    public static void bubbleSort(int[] array){
        int length = array.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                int tmp = 0;
                if(array[j] > array[j + 1]){
                    tmp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
        System.out.println(Arrays.toString(array));
    }
}
