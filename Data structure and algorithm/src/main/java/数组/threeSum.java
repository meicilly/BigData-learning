package 数组;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 三数之和
 * 给定一个包含 n 个整数的数组nums，判断nums中是否存在三个元素 a，b，c ，使得a + b + c = 0 ？找出所有满足条件且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * 示例:
 * 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
 * 满足要求的三元组集合为：
 * [
 *   [-1, 0, 1],
 *   [-1, -1, 2]
 * ]
 * [-2,0,1,1,2]
 */


public class threeSum {
    public static void main(String[] args) {
        int[] nums = new int[]{-2,0,1,1,2};
        //threeSum1(nums);
        threeSum2(nums);
    }
    /**
     * 方法一暴力破解法 时间复杂度O(n^3)
     */
    public static List<List<Integer>> threeSum1(int[] nums){
        int n = nums.length;
        ArrayList<List<Integer>> array = new ArrayList<>();
        for (int i = 0; i < n-2; i++) {
            for (int j = i+1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    if(nums[i] + nums[j] + nums[k] == 0) {
                        array.add(
                                Arrays.asList(nums[i],nums[j],nums[k])
                        );
                    }

                }
            }
        }
        System.out.println(array);
        return array;
    }
    /**
     * 方法二 双指针
     */
    public static List<List<Integer>> threeSum2(int[] nums){
        ArrayList<List<Integer>> array = new ArrayList<>();
        // TODO: 2023-03-16 对数组先进行排序
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // TODO: 2023-03-16 第一个元素都是大于>0的后面的元素相加就不可能为0 
            if(nums[i] > 0){
                break;
            }
            // TODO: 2023-03-16 如果元素相同则跳出循环 如果i=0的时候就是-1了所有要大于零
            if(i > 0 && nums[i] == nums[i-1]) continue;
            // TODO: 2023-03-16 i是固定点 其它两个点移动 处理成类似于两数和的问题来解决
            int lp = i + 1;
            int rp = n - 1;
            // TODO: 2023-03-16 循环遍历
            while (lp < rp){
                int sum = nums[i] + nums[lp] + nums[rp];
                if(sum == 0){
                    array.add(Arrays.asList(nums[i],nums[lp],nums[rp]));
                    while (lp < rp && nums[lp] == nums[lp + 1]){
                        lp++;
                    }
                    while (lp < rp && nums[rp] == nums[rp - 1]) {
                        rp--;
                    }
                    lp++;
                    rp--;
                } else if (sum < 0) {
                    lp++;
                }else if(sum >0){
                    rp--;
                }
            }
        }
        System.out.println(array);
        return array;
    }

}
