package 数组;

import java.util.Arrays;

public class nextPermutation {
    public static void nextPermutation(int[] nums) {
        int k = nums.length - 2;
        // TODO: 2023-03-17 k=1时跳出循环 此时nums[i]=3
        while(k > 0 && nums[k] > nums[k+1]) k--;
        // TODO: 2023-03-17 特殊情况 如果全部降序的情况下
        if(k < 0){
          Arrays.sort(nums);
          return;
        }
        int i = k + 2;
        // TODO: 2023-03-17 找到比3大的最小的数 i=4的时候
        while (i < nums.length && nums[i] > nums[k]) i++;
        // TODO: 2023-03-17 交换 1,3,8,7,6,2 -> 1,6,8,7,3,2
        int tmp;
        tmp = nums[i -1];
        nums[i-1] = nums[k];
        nums[k] = tmp;
        // TODO: 2023-03-17 将剩余 部分反转
        int start = k + 1, end = nums.length - 1;
        while( start < end ){
            int reTemp = nums[start];
            nums[start] = nums[end];
            nums[end] = reTemp;
            start++;
            end--;
        }



        System.out.println(Arrays.toString(nums));

    }

    public static void main(String[] args) {
        // TODO: 2023-03-17
        //  从k=num.length - 2 位开始 也就是6开始比较前一位
        //  如果比较结果大 那么k-- 直到k=1结束循环 i=k+2

        int[] ints = {1,3,8,7,6,2};
        nextPermutation(ints);
    }
}
