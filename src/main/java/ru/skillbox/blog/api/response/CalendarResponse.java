package ru.skillbox.blog.api.response;

import java.util.List;
import java.util.Map;

public class CalendarResponse {

    private List<Integer> years;
    private Map<String, Long> posts;

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    public Map<String, Long> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Long> posts) {
        this.posts = posts;
    }
}
