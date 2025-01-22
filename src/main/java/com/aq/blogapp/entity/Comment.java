package com.aq.blogapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  private String commentString;

  private String date;

  @ManyToOne
  @JoinColumn(name = "user_id")
//    @ToString.Exclude
  private User user;

  @ManyToOne
  @JoinColumn(name = "blog_id")
//    @ToString.Exclude
  private Blog blog;

}