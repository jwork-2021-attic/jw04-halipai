package com.cobini.calabashbros;

public class SSorter<T extends Comparable<T>> implements Sorter<T> {
    private T[] a;

    @Override
    public void load(T[] a) {
        this.a = a;
    }

    private void swap(int i, int j) {
        T temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
    }

    private String plan = "";

    @Override
    public void sort() {
        int n = a.length;
        for(int i = 0; i < n; i++){
            int min = i;
            for(int j = i+1; j < n; j++){
                if(a[j].compareTo(a[min]) < 0)   min = j;
            }
            swap(i, min);
        }
    }

    @Override
    public String getPlan() {
        return this.plan;
    }
}