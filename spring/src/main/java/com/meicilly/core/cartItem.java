package com.meicilly.core;


import lombok.Setter;

@Setter
public class cartItem {
    private Book book;
    private Integer count;
    private Integer amount;
    public Book getBook(){
        System.out.println(book.getTitle());
        return book;
    }

    @Override
    public String toString() {
        return "cartItem{" +
                "book=" + book +
                ", count=" + count +
                ", amount=" + amount +
                '}';
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
