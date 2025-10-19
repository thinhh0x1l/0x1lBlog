package top.blogapi.util;

import org.springframework.util.DigestUtils;

import java.util.*;

public class MD5Utils {
    private static final String SALT = "mySecret";

    public static String getMD5(CharSequence str) {
        String base = SALT + str;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    public static void main(String[] args) {
        System.out.println(RLE(7));
        //System.out.println(ab(-7,5));
   //     System.out.println(findSmallestInteger2(new int[]{3,0,3,2,4,2,1,1,0,4},5));
//        Map<Integer,Integer> map = new HashMap<>();
//        map.put(1,1);
//        map.computeIfPresent(1,(k,v)->v++);
//        System.out.println(map.get(1));
        //System.out.println(findSmallestInteger(new int[]{3,2,3,1,0,1,4,2,3,1,4,1,3},5));
    }
    public  static int findSmallestInteger2(int[] nums, int value) {
        Map<Integer,Integer> map = new HashMap<>();

        boolean[] m = new boolean[nums.length+1];

        for(int i = 0 ; i < nums.length ;i++){

            if(nums[i] < 0){
                nums[i] += value*((-nums[i] / value)+1) ;
            }
            int d = nums[i] % value;
            if(!map.containsKey(d)){
                if(d < m.length){
                    map.put(d,1);
                    System.out.println("d: "+ d + " nums[i]: "+nums[i]);
                    m[d] = true;
                }

            }
            else{
                map.computeIfPresent(d,(k,v)->++v);
                int r = value*map.get(d);
                System.out.println("r: "+ r+ " nums[i]: "+nums[i]);
                if(r < m.length)
                    m[r] = true;
            }

        }

        for(int i = 0 ; i < nums.length;i++){
            if(!m[i]){
                return i;
            }

        }
        return nums.length;
    }
    public static int findSmallestInteger(int[] nums, int value) {
        Arrays.sort(nums);
        Set<Integer> set = new HashSet<Integer>();
        boolean[] m = new boolean[nums.length+1];

        for(int i = 0 ; i < nums.length ;i++){
            System.out.println(nums[i] + ": ");
            if(nums[i] < 0)
                nums[i] *= -1;
            int d = nums[i] % value;
            while(d < m.length){
                if(!set.contains(d)){
                    set.add(d);
                    m[d] = true;
                    break;
                }
                System.out.print(d +" ");
                d+= value;
            }
            System.out.println();
        }

        for(int i = 0 ; i < nums.length;i++){
            if(!m[i]){
                System.out.println("yeas");
                return i;
            }

        }
        return nums.length;
    }
    public static int ab (int num,int value){
        return num + value*((-num / value)+1);
    }

    public static String RLE(int n){
        StringBuilder sb = new StringBuilder("1");
        for (int i = 2 ; i <= n ; i++){
            int count= 1;
            char digit = sb.charAt(0);
            StringBuilder sbT = new StringBuilder();
            for(int j = 1 ; j < sb.length() ; j++){
                if(digit == sb.charAt(j)){
                    count++;
                }else{
                    sbT.append(count).append(digit);
                    count = 1;
                    digit = sb.charAt(j);
                }
            }
            sbT.append(count).append(digit);
            sb=sbT;
        }
        return sb.toString();
    }
}
