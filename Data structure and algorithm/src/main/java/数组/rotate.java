package 数组;

public class rotate {
    public static void main(String[] args) {
        /**
         *  {1,2,3},   {1,4,7}, (0,1) -> (1,0)
         *  {4,5,6},=> {2,5,8},
         *  {7,8,9}    {3,6,9}
         */
        /** 目标
         *  [7,4,1], (0,0) -> (0,2)
         *  [8,5,2],  (1,0)-> (1,3)
         *  [9,6,3]
         */

        int[][] image1 = {
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };
        int[][] image2 = {
                {5,1,9,11},
                {2,4,8,10},
                {13,3,6,7},
                {15,14,12,16}
        };
        rotate(image1);
        printImage(image1);
        //System.out.println(3/2);

    }
    public static void rotate(int[][] matrix){
        int n = matrix.length;
        // TODO: 2023-03-20 先遍历行 再遍历列
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                   int tmp = matrix[i][j];
                   matrix[i][j] = matrix[j][i];
                   matrix[j][i] = tmp;
            }
        }
        // TODO: 2023-03-20 前后位置对换
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n/2; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }
    public static void printImage(int[][] image){
        System.out.println("image:");
        for (int[] line:image) {
            for (int point: line) {
                System.out.print(point+"\t");
            }
            System.out.println();
        }
    }

}
