package com.steam.game.caster;

public class Selection {

    static void select2(int[] arr){
        for (int i = 0; i < arr.length-1; i++) {
            int minPosition = i;
            for (int j = i+1; j < arr.length; j++) {

               minPosition = arr[j] <arr[minPosition]? j:minPosition;
//                if (arr[j] < arr[minPosition])
//                    minPosition = j;
            }
//               swap(arr,i,minPosition);
               swap(arr,minPosition,i);

                System.out.println("minPosition:"+minPosition);

        }
        print(arr);
    }

    static void select1(int[] arr){

        for (int i = 0; i < arr.length-1; i++) {

            int minPosition = 0;
            if (arr[i]<arr[minPosition]) {
                minPosition = i;
            }

            int temp =arr[0];

            arr[0] = arr[minPosition];

            arr[minPosition]=temp;

            System.out.println("minPosition:"+minPosition);


        }
        print(arr);
    }

    static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]+ " ");
        }
    }


    static void swap(int[] arr,int i,int j){
        int temp =arr[i];
        arr[i] = arr[j];
        arr[j]=temp;
    }

    public static void main(String[] args) {


        int[] arr = {3,2,1,9,0,8};

//        select1(arr);
        select2(arr);
    }
}
