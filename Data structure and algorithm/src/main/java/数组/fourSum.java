package 数组;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fourSum {
    /**
     * 输入：nums = [1,0,-1,0,-2,2], target = 0
     * 输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
     *
     * 输入：nums = [2,2,2,2,2], target = 8
     * 输出：[[2,2,2,2]]
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        // TODO: 2023-03-21 判断如果nums是空返回 如果数组长度小于四返回
        if(nums == null || nums.length < 4){
            return result;
        }
        Arrays.sort(nums);
        int length = nums.length;
        // TODO: 2023-03-21 类似于三数之和的思想
        for (int i = 0; i < length -3; i++) {
            // TODO: 2023-03-21 判断相邻的元素是否相同 如果相同则跳出循环进行下一次的循环
            if(nums[i] == nums[i-1]){
                continue;
            }
            // TODO: 2023-03-21 如果前四个数加起来就大于目标的值 那就算找完了也没有对应的target 
            if(nums[i] + nums[i + 1] + nums[i +2] + nums[i + 3] > target){
                return result;
            }
            // TODO: 2023-03-21 最后四个元素相加也小于target则跳出循环 进入下一步循环
            if(nums[i] + nums[length - 1] +nums[length - 2] + nums[length - 3] < target){
                continue;
            }
            for (int j = i +1; j < length - 2; j++) {
                // TODO: 2023-03-21 判断相邻元素是否相同 相同则跳出此次循环 进行下一次的循环
                if(j > i +1 && nums[j] == nums[j - 1]){
                    continue;
                }
                // TODO: 2023-03-21 如果 i + j + (j +1) + (j + 2)的值大于目标值则终止此次循环 因为往后走不可能找到更小的数了
                if(nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target){
                    break;
                }
                // TODO: 2023-03-21
                if(nums[i] + nums[j] + nums[length - 1] + nums[length - 2] < target){
                    continue;
                }
                // TODO: 2023-03-21 现在完全就可以想成三数和的思想了
                int left = j + 1, right = length - 1;
                while (left < right){
                    int sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if(sum == target){
                        result.add(Arrays.asList(nums[i],nums[j],nums[left],nums[right]));
                        while (left < right && nums[left] == nums[left + 1]){
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1]){
                            right--;
                        }
                        left++;
                        right--;
                    }else if(sum < target){
                        left++;
                    }else {
                        right--;
                    }

                }
            }
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
