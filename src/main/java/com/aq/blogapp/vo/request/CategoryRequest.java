package com.aq.blogapp.vo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryRequest {

    @NotEmpty
    @Size(min = 4, message = "Category title must be at least 4 characters")
    private String categoryTitle;

    @NotEmpty
    @Size(min = 25, message = "Category description must be at least 25 characters")
    private String categoryDescription;

}
