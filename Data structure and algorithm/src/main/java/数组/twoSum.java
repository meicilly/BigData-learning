package 数组;

import java.util.HashMap;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
public class twoSum {

    public static void main(String[] args) {
        int[] array = new int[]{1,23,42,2,3,7};
        twoSum1(array,9);
        twoSum2(array,9);
        twoSum3(array,5);
    }
    /**
     * 方法一 暴力破解法
     * 时间复杂度为O(n^2)
     */
    public static int[] twoSum1(int[] nums,int target){
        long start = System.currentTimeMillis();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if(nums[i] + nums[j] == target){
                    return new int[]{i,j};
                }
            }
        }
        //throw new IllegalArgumentException("No two sum solution");
        return  new int[]{0};
    }
    /**
     * 方法二 两次遍历hash
     * 时间复杂度为O(n)
     */
    public static int[] twoSum2(int[] nums,int target){
        HashMap<Integer, Integer> map = new HashMap<>();
        // TODO: 2023-03-16 保留到hasMap中
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }
        // TODO: 2023-03-16  挨个遍历数组查找
        for (int i = 0; i < nums.length; i++) {
            int thatNum = target - nums[i];
            if (map.containsKey(thatNum) && map.get(thatNum) != i){
                System.out.println(i + "和" + map.get(thatNum));
                return new int[]{i,map.get(thatNum)};
            }
        }
        throw new IllegalArgumentException("没有找到");
    }
    /**
     * 方法三 一次hash遍历
     * 时间复杂度为O(1)
     */
    public static int[] twoSum3(int[] nums,int target){
        HashMap<Integer, Integer> map = new HashMap<>();
        // TODO: 2023-03-16
        for (int i = 0; i < nums.length; i++) {
            int thatNum = target - nums[i];
            if(map.containsKey(thatNum) && map.get(thatNum) != i) {
                System.out.println(i+""+map.get(thatNum));
                return new int[]{i,map.get(thatNum)};
            }
            map.put(nums[i],i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
