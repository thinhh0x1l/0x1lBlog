package top.blogapi.util;

import org.springframework.util.DigestUtils;

import java.util.*;

public class MD5Utils {
    private static final String SALT = "mySecret";

    public static String getMD5(CharSequence str) {
        String base = SALT + str;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
    static int index = 0;
    public static String decodeString(Str str) {
        StringBuilder sb = new StringBuilder();
        char[] ar = "aaaa3[a2[c]]3[b2[d]]aaaaaaaaaaaa".toCharArray();
        while(ar[index] >= 'a' && ar[index] <= 'z' ){
            System.out.println("");
            sb.append(ar[str.index++]);
        }
        return sb.append(gen(str,3-2,ar)).toString();

    }
    static class Str{
        String str;
        int index;
    }
    public static String gen(Str str, int times, char[] ar){
        StringBuilder sb = new StringBuilder();

        System.out.println(ar[str.index]);
        while(str.index< ar.length&&ar[str.index] != ']'){
            if(ar[str.index] >= 'a' && ar[str.index] <= 'z')
                sb.append(ar[str.index]);
            else {
                str.index+=2;
                sb.append(gen(str,ar[str.index-2]-'0', ar));
            }
            str.index++;
        }
        String s = sb.toString();
        System.out.println(s);
        return sb.repeat(s,times-1).toString();
    }
    public static void main(String[] args) {
        System.out.println(3>>1);
    }
    public static boolean hasSameDigits(String s) {
        int leng = s.length();
        int j = 1 ;
        char[] c = s.toCharArray();
        while(j<leng-1){
            for(int i = leng-1 ; i>=j;i--){
                System.out.println(((c[i]+c[i-1])-(2*'0'))%10);
                char c1 = (char)(((c[i]+c[i-1])-(2*'0'))%10+'0');
                char c2 = 65;
                System.out.println(c1);
                c[i] = c1;
            }
            System.out.println(new String(c));
            j++;
        }
        return c[leng-2] ==c[leng-1];
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
