package ru.skillbox.blog.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CalendarDto {

    private List<Integer> years;
    private Map<LocalDate, Long> posts;

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public Map<LocalDate, Long> getPosts() {
        return posts;
    }

    public void setPosts(Map<LocalDate, Long> posts) {
        this.posts = posts;
    }
}
