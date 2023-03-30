package 二分查找;

public class SearchMatrix {
    public boolean searchMatrix(int[][] matrix, int target){
        int m = matrix.length;
        if(m == 0 ) return  false;
        int n = matrix[0].length;
        int left = 0;
        int right = m*n - 1;
        // TODO: 2023-03-22 左右指针移动
        while (left <= right){
            int mid = (left + right) / 2;
            if(mid < target){
                left = mid + 1;
            } else if (mid > target) {
                right = mid -1;
            }else {
                return  true;
            }
        }
        return  false;
    }
}
