package 二分查找;

import com.sun.org.apache.bcel.internal.generic.IINC;

import java.util.HashMap;

public class findDuplicate {
    /**
     *
     * @param nums
     * @return
     * 示例 1:
     * 输入: [1,3,4,2,2]
     * 输出: 2
     * 示例 2:
     * 输入: [3,1,3,4,2]
     * 输出: 3
     */
    public static int findDuplicate(int[]  nums){
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num:nums) {
            if(map.get(num) == null){
                map.put(num,1);
            }else {
                return num;
            }
        }
        return -1;
    }
}
