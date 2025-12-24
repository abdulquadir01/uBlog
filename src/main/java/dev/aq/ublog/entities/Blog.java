package dev.aq.ublog.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "blogs")
public class Blog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long blogId;

  @NotEmpty
  @Column(name = "blog_title", nullable = false, length = 256)
  private String title;

  @NotEmpty
  @Column(name = "blog_content", nullable = false, length = 4096)
  private String content;

  @Column(name = "blog_image", nullable = false, length = 128)
  private String imageName = "";

  @Column(name = "blogging_date", nullable = false, length = 128)
  private String bloggedDate = "";

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    Blog blog = (Blog) o;
    return getBlogId() != null && Objects.equals(getBlogId(), blog.getBlogId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
