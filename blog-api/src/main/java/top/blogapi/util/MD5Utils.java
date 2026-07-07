package top.blogapi.util;

import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Tiện ích băm MD5 với tiền tố salt bí mật. */
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


    int t = 4;
    class A{

        int l;
        A(int l){
            this.l = l + t;
        }
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

    static class Node{
        int val;
        Node next;
        Node(int val){
            this.val = val;
        }
        Node(int val,Node next){
            this.val = val;
            this.next = next;
        }
        Node(){}
    }


    public static long getRGBbyte(int red, int green, int blue) {
        return (red* 65536L)+(green* 256L)+blue ;
    }


    public static void extendGcd(){
        int[] a = new int[]{2,5,1,2,3,1,1,4,1};
        int[] b = new int[]{1,6};
        int j = 1;
        for(int i = 0 ; i < a.length ; i++){
            b[i&1] = b[(i+1) & 1] * a[i] + b[i&1];
            System.out.print(32321*-b[0]+b[1]*26513 + " == ");
            System.out.print(32321*b[0]-b[1]*26513  + " == ");
            System.out.print(32321*-b[1]+b[0]*26513 + " == ");
            System.out.print(32321*b[1]-b[0]*26513  + " == ");
            System.out.println(b[0] + " | " + b[1]);
        }

        System.out.print(32321L*-b[1]+b[0]*26513L + " == ");
    }

    public static void gcd(){
        int b = 26513;
        int a = 32321;
        while(b != 0){
            int temp = a % b;
            System.out.println(a + " = " + (a/b)+"("+b+") + " + temp);
            a = b;
            b = temp;

        }
        System.out.println(a);
    }

    public static void XOR_you_dont(){
        char[] cipher = "0e0b213f26041e480b26217f27342e175d0e070a3c5b103e2526217f27342e175d0e077e263451150104".toCharArray();
        byte[] crypto = "crypto{".getBytes();
        char[] k = "0e0b213f26041e".toCharArray();
        StringBuilder guestKey = new StringBuilder();
        StringBuilder flag = new StringBuilder();

        for(int i =0 ; i < crypto.length ; i++){
            guestKey.append((char)(crypto[i]^Integer.parseInt(k[i*2]+""+k[i*2+1],16)));
        }
        System.out.println(guestKey.toString()) ;// myXORke -> myXORkey
        byte[] key = guestKey.append("y").toString().getBytes();
        for(int i = 0 ; i < cipher.length ; i+=2 ){
            flag.append((char)(key[i/2%key.length]^Integer.parseInt(cipher[i]+""+cipher[i+1],16)));
        }
        System.out.println(flag.toString()) ;
    }

    public static void Favourite_byte(){
        BigInteger flag = new BigInteger("0e0b213f26041e480b26217f27342e175d0e070a3c5b103e2526217f27342e175d0e077e263451150104",16);
        System.out.println(flag.toString());
        System.out.println(flag.toString(16));
        System.out.println("0e0b213f26041e480b26217f27342e175d0e070a3c5b103e2526217f27342e175d0e077e263451150104".length());
        for(int z = 0 ; z <=255;z++){
//            System.out.println(flag.xor(new BigInteger(Integer.toBinaryString(i))));
            BigInteger x = new BigInteger(z+"");
            char[] c = "0e0b213f26041e480b26217f27342e175d0e070a3c5b103e2526217f27342e175d0e077e263451150104".toCharArray();
//            for(int p = 0 ; p < c.length ; p++)
//                c[p] = (char)(c[p]^z);
            StringBuilder sb = new StringBuilder();
            for(int i = 0 ; i < c.length;i+=2){
                sb.append((char)(Integer.parseInt((c[i])+""+(c[i+1]),16)^z) );
            }
//            c = sb.toString().toCharArray();
//            for(int i = 0 ; i < c.length;i++){
//                c[i] = (char)(c[i]^ z);
//            }
//            System.out.println(z+ ": " +  new String(c));
            System.out.println(sb);
        }
    }

    public static String XOR_Properties(String key1, String key2, String fl){
        BigInteger key1B = new BigInteger(key1,16);
        BigInteger key2B = new BigInteger(key2,16);
        BigInteger flag = new BigInteger(fl,16);

        flag = flag.xor(key1B).xor(key2B);
        System.out.println("xor: " +flag.toString(16));
        System.out.println(flag.toString(16).length());
        char[] c = flag.toString(16).toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < c.length;i+=2){
            sb.append((char)Integer.parseInt(c[i]+""+c[i+1],16) );
        }
        return sb.toString();
    }

    public static String XOR_Starter(String s, int i){
        if(i == s.length()){
            return "";
        }
        return (char)(s.charAt(i)^13)+XOR_Starter(s, i+1);
    }

    public static void de(){
        Node head = null;
        Node temp = null;
        for(int i = 1; i <= 5;i++){
            Node newNode = new Node(i);
            if(i==1){
                head = (temp=newNode);
            }
            temp.next = newNode;
            temp = temp.next;
        }
        temp=head;
        while(temp!=null){
            System.out.print(temp.val + " -> ");
            temp = temp.next;
        }
        Node h = head;
        Node c = null;
        Node prev = (temp=head);
        System.out.println();
        for(int z = 1; z >0 ; z--){
            prev = temp;
            for(int i = 1 ;i <= 3; i++){
                Node next = h.next;
                h.next = temp;
                temp = h;
                h = next;
            }
            if(z==1){
                head.next = h;
                c=head;
                head=temp;
            }else{
                c.next =temp;
                c= prev;
            }
            System.out.print(temp.val + " -> ");
            temp = h;

        }
        System.out.println("/n " + h.val);
        prev.next=h;


        temp=head;
        while(temp!=null){
            System.out.print(temp.val + " -> ");
            temp = temp.next;
        }
    }
    public static void modular(){
        long x= 1;
        int y = 5;
        for(int i = 1; i <= 16 ; i++){
            x *=y;
        }
        System.out.println(x%17 );
    }
    public static boolean kLengthApart(int[] nums, int k) {
        int i =0;
        while(i< nums.length && nums[i] == 1){
            if(nums[i] == 1 )
                break;
            i++;
        }
        int j = i++;
        while(i < nums.length){
            if(nums[i] == 1){
                if(i-j-1>=k){
                    j = i;
                }else{
                    return false;
                }
            }
            i++;
        }
        return true;
    }

    public static void findFinalValue(){

        int[] nums =  new int []{8,19,4,2,15,3};
        int original = 2;

        int j = 0;
        int[][] map = new int [nums.length][2];
        for (int i = 0; i < nums.length; i++) {
            System.out.println(original<<i);
            System.out.println((19>>i) +"f");
        }
    }
    public static int specialTriplets(int[] nums) {
        long count = 0;
        Map<Integer, Integer> map2 = new HashMap<>();
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(nums[0],1);
        for(int i = 2; i < nums.length; i++){
            map2.put(nums[i], map2.getOrDefault(nums[i], 0) + 1);
        }
        for (int i = 1; i < nums.length -1; i++) {
            int key = nums[i] * 2;
            if(map2.containsKey(key) && map1.containsKey(key)){
                count += (long) map1.get(key) * map2.get(key);
            }
            map1.put(nums[i], map1.getOrDefault(nums[i], 0) + 1);
            map2.computeIfPresent(nums[i + 1] , (k,v)->  v > 1 ? v - 1 : null);
        }
        return (int) count;
    }
    public static boolean matchesWordPattern(String input) {
        // Tương đương với Pattern.matches("[\\w]+", input)
        if (input == null || input.isEmpty()) {
            return false; // [\w]+ yêu cầu ít nhất 1 ký tự
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // Kiểm tra ký tự thuộc \w (word character):
            // 1. Chữ cái: a-z, A-Z
            // 2. Số: 0-9
            // 3. Dấu gạch dưới: _
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }

        return true;
    }

    public static void backtrack(List<Integer> list , int k,int n){
        if(list.size() == n)
            return;
        for(int i = list.getLast()+1 ; i <= n+1 ; i++){
            list.add(i);
            backtrack(list , k , n);
            list.removeLast();
        }
    }

    public static List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();

        for(int i = 1 ; i <= n-k+1 ; i++){
            List<Integer> list = new ArrayList<>();
            list.add(i);
            backtrack(list,k,n);
            ans.add(list);
        }
        return ans;
    }
    public static int totalSteps( int[] nums) {
        int ans = 0;
        int max = nums[0];
        int tempMax = 0;
        for (int i = 1; i < nums.length; i++) {
            if(nums[i] >= max){
                max = nums[i];
                ans = Math.max(ans, tempMax);
                tempMax = 0;
            }else{
                tempMax++;
            }
        }
        return Math.max(ans, tempMax);

    }

    public static void smallerNumbersThanCurrent(int[] nums) {
        int[][] arr = new int[nums.length][3]; //0.val,1.pre,2.index;
        for(int i= 0 ; i< nums.length ; i++){
            arr[i][0] = nums[i];
            arr[i][2] = i;
        }
        Arrays.sort(arr,(a1,a2) -> a1[0]-a2[0]);
        for(int i = 1 ; i < nums.length ; i++){
            if(arr[i][0] > arr[i-1][0])
                arr[i][1] = i;
            else
                arr[i][1] = arr[i-1][1];
        }
        int[] ans = new int[nums.length];
        for(int[] a : arr)
            ans[a[2]] = a[1];
        for (int[] a: arr)
            System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(ans));

    }
    static List<Integer> sfsd (){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        return list;
    }
    static List<Integer> st(){
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        System.out.println(stack);

        stack.pop();
        System.out.println(stack);
        return stack;
    }


    public static void hello() throws InterruptedException {
        String text = "hello world";
        int length = text.length();

        // Mảng lưu màu hiện tại của mỗi ký tự
        int[] currentColors = new int[length];

        // Khởi tạo màu ngẫu nhiên cho mỗi ký tự
        for (int i = 0; i < length; i++) {
            currentColors[i] = 31 + (int)(Math.random() * 7);
        }

        System.out.print("\033[?25l"); // Ẩn con trỏ

        while (true) {
            // Di chuyển con trỏ về đầu dòng
            System.out.print("\r");

            // In từng ký tự với màu của nó
            for (int i = 0; i < length; i++) {
                // Thay đổi màu ngẫu nhiên cho ký tự này
                if (Math.random() > 0.7) { // 30% cơ hội đổi màu
                    currentColors[i] = 31 + (int)(Math.random() * 7);
                }

                // In ký tự với màu hiện tại
                System.out.print("\u001B[" + currentColors[i] + "m" + text.charAt(i));
            }

            System.out.print("\u001B[0m"); // Reset
            Thread.sleep(200); // Tốc độ thay đổi
        }
    }

    public static void maximalRectangle(int[][] matrix) {
        int[][]dp= new int[matrix.length+1][matrix[0].length+1];
        for(int i = 1 ; i< dp.length;i++){

            for(int j = 1 ; j < dp[0].length;j++){
                int k = matrix[i-1][j-1];
                dp[i][j] = (dp[i-1][j] + k)*k ;
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void maxSideLength(int[][] mat, int threshold) {
        int[][] mati = new int[mat.length][mat[0].length];
        for(int i = 0 ; i < mat.length ; i++){
            mati[i][0] = mat[i][0];
            for(int j = 1; j < mat[0].length ;j++)
                mati[i][j] = mati[i][j-1] + mat[i][j];
        }
        for (int[] m : mati)
            System.out.println(Arrays.toString(m));
        System.out.println();
        for(int j = 0 ; j < mati[0].length;j++){
            for (int i = 1 ; i < mati.length;i++){
                mati[i][j] += mati[i-1][j];
            }
        }
        for (int[] m : mati)
            System.out.println(Arrays.toString(m));
    }
    public static int[] findEvenNumbers(int[] digits) {
        List<String> sT = new ArrayList<>();
        List<Integer> digit = new LinkedList<>();
        for(int d : digits)
            digit.add(d);
        StringBuilder sb = new StringBuilder();
        sb.append(digit.removeFirst());
        for(int i = 1; i < digits.length ;i++){

        }
        int[] ans = null;
        return ans;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(findEvenNumbers(new int[]{2, 1, 3, 0}));

    }
}
