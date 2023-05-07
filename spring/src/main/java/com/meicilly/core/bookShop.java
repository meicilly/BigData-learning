package com.meicilly.core;

import java.util.List;
import java.util.Map;

public class bookShop {
    private List<Book> books;
    private Map<String,String> users;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "bookShop{" +
                "books=" + books +
                ", users=" + users +
                '}';
    }
}
