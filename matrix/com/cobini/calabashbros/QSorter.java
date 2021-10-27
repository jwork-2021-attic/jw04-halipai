package com.cobini.calabashbros;

public class QSorter<T extends Comparable<T>> implements Sorter<T> {

    private T[] a;

    @Override
    public void load(T[] a) {
        this.a = a;
    }

    int p = 0;
    int q = 0;
    private void swap(int i, int j) {
        p++;
        System.out.println(p);
        T temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
    }

    private String plan = "";

    public void quick(int left, int right){
        //System.out.println(left+"<->"+right);
        if(left >= right)    return;
        T base = a[left];
        int i = left, j = right;
        while(i < j){
            while(i < j && a[j].compareTo(base) > 0){
                j--;
            }
            while(i < j && a[j].compareTo(base) <= 0){
                i++;
            }
            if(i < j){
                swap(i, j);
            }
        }
        if(i != left){
            swap(left, i);
        }
        quick(left, i-1);
        quick(i+1, right);
    }

    @Override
    public void sort() {
        int n = a.length;
        quick(0, n-1);
    }

    @Override
    public String getPlan() {
        return this.plan;
    }

}