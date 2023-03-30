package 二分查找;

public class binarySearch {
    public static int binarySearch(int[] a, int key){
        int low = 0;
        int high = a.length - 1;
        // TODO: 2023-03-22 比较最大最小的与最大的值 查看数组中是否有这个元素
        if(key < a[low] || key > a[high]) return  -1;
        // TODO: 2023-03-22 满足条件开始left++ right--查找元素
        while (low <= high){
            // TODO: 2023-03-22 找到中间元素
            int mid = (low + high) / 2;
            if(a[mid] < key){
                low = mid + 1;
            }else if(a[mid] > key){
                high = mid - 1;
            } else
                return mid;

        }
        return -1;
    }
}
