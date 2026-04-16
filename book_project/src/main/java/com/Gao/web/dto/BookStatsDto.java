package com.Gao.web.dto;

public class BookStatsDto {

    private int titleCount;
    private int titlesWithAvailableCopies;
    private int titlesFullyBorrowed;

    public int getTitleCount() {
        return titleCount;
    }

    public void setTitleCount(int titleCount) {
        this.titleCount = titleCount;
    }

    public int getTitlesWithAvailableCopies() {
        return titlesWithAvailableCopies;
    }

    public void setTitlesWithAvailableCopies(int titlesWithAvailableCopies) {
        this.titlesWithAvailableCopies = titlesWithAvailableCopies;
    }

    public int getTitlesFullyBorrowed() {
        return titlesFullyBorrowed;
    }

    public void setTitlesFullyBorrowed(int titlesFullyBorrowed) {
        this.titlesFullyBorrowed = titlesFullyBorrowed;
    }
}
