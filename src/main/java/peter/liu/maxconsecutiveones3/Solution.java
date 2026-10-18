package peter.liu.maxconsecutiveones3;

public class Solution {
    public int longestOnes(int[] nums, int k) {
         int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) k--;      // 花掉一次翻转额度
            if (k < 0) {                    // 额度不够：窗口整体右移，宽度保持不变
                if (nums[left] == 0) k++;   // 左端移出的 0 还回额度
                left++;
            }
        }
        return nums.length - left;          // 最终窗口宽度就是历史最大值
    }
}

