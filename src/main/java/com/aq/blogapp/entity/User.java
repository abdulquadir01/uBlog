package com.aq.blogapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, length = 64)
  private String firstName;

  @Column(nullable = false, length = 64)
  private String lastName;

  @Column(nullable = false, length = 64)
  private String email;

  @Column(nullable = false, length = 64)
  private String password;

  @Column(nullable = false, length = 4096)
  private String about;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Blog> blogs = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @ToString.Exclude

  private Set<Comment> comments = new HashSet<>();

  @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name = "user", referencedColumnName = "userId"),
    inverseJoinColumns = @JoinColumn(name = "roles", referencedColumnName = "roleId")
  )
  private Set<Role> roles = new HashSet<>();


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return userId != null && Objects.equals(userId, user.userId);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }


  //  UserDetails
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    return roles.stream()
      .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
      .toList();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
