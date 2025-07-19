package com.example.tictactoe.utils;

public class MyArray {
    final int MAX_ELEMENTS = 50;
    long[] numbers = new long[MAX_ELEMENTS];
    public int count = 0;
    long max = Long.MIN_VALUE;
    long min = Long.MAX_VALUE;

    public void addNumber(long number) {
        numbers[count] = number;
        count ++;
        if (number > max) {
            max = number;
        }
        if (number < min) {
            min = number;
        }
    }

    public boolean isSubset(MyArray other) {
        boolean res = false;
        if (other.count < this.count) {
            return res;
        }
        for (int i = 0; i < this.count; i++ ) {
            if (!other.hasNumber(this.numbers[i])) {
                return res;
            }
        }
        res = true;
        return res;
    }

    public boolean hasNumber(long n) {
        boolean result = false;
        for (int i = 0; i < this.count; i++) {
            if (n == numbers[i]) {
                result = true;
                break;
            }
        }
        return result;
    }

    public MyArray substract(MyArray other) {
        MyArray res = new MyArray();
        for (int i = 0; i < this.count; i++) {
            if (!other.hasNumber(this.numbers[i])) {
                res.addNumber(this.numbers[i]);
            }
        }
        return res;
    }

    public void clear() {
        count = 0;
        for (int i = 0; i < MAX_ELEMENTS; i++) {
            numbers[i] = 0;
        }
    }

    public int itemAt(int index) {
        return (int)numbers[index];
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < count; i++) {
            System.out.print(numbers[i] + " ");
        }
        System.out.println();
    }
}
