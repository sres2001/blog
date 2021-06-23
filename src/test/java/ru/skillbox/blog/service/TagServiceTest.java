package ru.skillbox.blog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillbox.blog.dto.TagDto;
import ru.skillbox.blog.repository.TagRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagServiceTest {

    @Autowired
    private TagService service;

    @Autowired
    private TagRepository repository;

    @Test
    @DisplayName("Находим все тэги")
    public void testListTagsWithWeights_All() {
        List<TagDto> dtoList = service.listTagsWithWeights(null);
        assertFalse(dtoList.isEmpty());

        Set<String> tags = dtoList.stream()
                .map(TagDto::getName)
                .collect(Collectors.toSet());

        repository.findAll().forEach(e -> assertTrue(tags.contains(e.getName())));
    }

    @Test
    @DisplayName("Находим тэг Java")
    public void testListTagsWithWeights_Java() {
        List<TagDto> dtoList = service.listTagsWithWeights("Ja");
        assertEquals(1, dtoList.size());
        assertEquals("Java", dtoList.get(0).getName());
    }
}
