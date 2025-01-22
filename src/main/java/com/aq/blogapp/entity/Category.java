package com.aq.blogapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;

  @Column(name = "title", nullable = false, length = 64)
  private String categoryTitle;

  @Column(name = "description", nullable = false, length = 256)
  private String categoryDescription;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Blog> blogs = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Category category = (Category) o;
    return categoryId != null && Objects.equals(categoryId, category.categoryId);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
