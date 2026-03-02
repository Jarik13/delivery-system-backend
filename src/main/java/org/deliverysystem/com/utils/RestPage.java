package org.deliverysystem.com.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestPage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;


    public RestPage(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public Page<T> toPage() {
        return new PageImpl<>(content, PageRequest.of(number, size), totalElements);
    }
}