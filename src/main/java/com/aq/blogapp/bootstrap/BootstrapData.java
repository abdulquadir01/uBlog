package com.aq.blogapp.bootstrap;

import com.aq.blogapp.constants.UBlogConstants;
import com.aq.blogapp.entity.Blog;
import com.aq.blogapp.entity.Category;
import com.aq.blogapp.entity.Role;
import com.aq.blogapp.entity.User;
import com.aq.blogapp.respositories.BlogRepository;
import com.aq.blogapp.respositories.CategoryRepository;
import com.aq.blogapp.respositories.RoleRepository;
import com.aq.blogapp.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.aq.blogapp.constants.UBlogConstants.LOREM_STRING;
import static com.aq.blogapp.constants.UBlogConstants.PASSWORD;


@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(BootstrapData.class);

  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final BlogRepository blogRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder pwdEncoder;

  @Override
  public void run(String... args) throws Exception {
    loadRoles();
    logger.info("User Role count in table roles : {}", roleRepository.count());

    loadUsers();
    logger.info("User count in table users : {}", userRepository.count());

    loadCategories();
    logger.info("Category count in table categories : {}", categoryRepository.count());

    loadBlogs();
    logger.info("Blog count in table blogs : {}", blogRepository.count());
  }

  private final User johnCena = new User();
  private final User johnDoe = new User();
  private final User johnWick = new User();
  private final User deadPool = new User();
  private final User byomukesh = new User();

  private Category sports = new Category();
  private Category tech = new Category();
  private Category movies = new Category();
  private Category novels = new Category();
  private Role normalUser;
  private Role adminUser;

  private void loadRoles() {

    try {
      normalUser = Role.builder()
        .roleCode(UBlogConstants.ROLE_NORMAL_CODE)
        .roleName("NORMAL_USER")
        .build();

      adminUser = Role.builder()
        .roleCode(UBlogConstants.ROLE_ADMIN_CODE)
        .roleName("ADMIN_USER")
        .build();

      List<Role> roles = List.of(normalUser, adminUser);

      List<Role> result = roleRepository.saveAll(roles);

      result.forEach(r -> logger.info(r.getRoleName()));
    } catch (Exception ex) {
      logger.warn(ex.toString());
    }
  }

  private void loadCategories() {
    this.sports = Category.builder()
      .categoryTitle("Sports")
      .categoryDescription("All the blogs related to sports")
      .build();

    this.tech = Category.builder()
      .categoryTitle("Technology")
      .categoryDescription("All the happenings in the tech world")
      .build();

    this.movies = Category.builder()
      .categoryTitle("Movies")
      .categoryDescription("Blogs about the best movies of all times")
      .build();

    this.novels = Category.builder()
      .categoryTitle("Novels")
      .categoryDescription("Blogs about the best novels of all times")
      .build();

    List<Category> categories = List.of(sports, tech, movies, novels);
    categoryRepository.saveAll(categories);
  }

  private void loadUsers() {

    johnCena.setFirstName("Jhon");
    johnCena.setLastName("Cena");
    johnCena.setEmail("johncena@wwe.org");
    johnCena.setPassword(pwdEncoder.encode(PASSWORD));
    johnCena.setAbout("This guy is a wrestler & an actor.");
    johnCena.getRoles().add(normalUser);

    johnDoe.setFirstName("Jhon");
    johnDoe.setLastName("Doe");
    johnDoe.setEmail("jdoe@wwe.org");
    johnDoe.setPassword(pwdEncoder.encode(PASSWORD));
    johnDoe.setAbout("This guy is a Cyber-security Professional");
    johnDoe.getRoles().add(normalUser);

    johnWick.setFirstName("Jhon");
    johnWick.setLastName("Wick");
    johnWick.setEmail("jwick@wwe.org");
    johnWick.setPassword(pwdEncoder.encode(PASSWORD));
    johnWick.setAbout("This guy is a Missionary");
    johnWick.getRoles().add(normalUser);

    deadPool.setFirstName("Dead");
    deadPool.setLastName("Pool");
    deadPool.setEmail("pooldead@gmail.com");
    deadPool.setPassword(pwdEncoder.encode(PASSWORD));
    deadPool.setAbout("This guy is a Mutant");
    deadPool.getRoles().add(normalUser);

    byomukesh.setFirstName("Byomukesh");
    byomukesh.setLastName("Bakshi");
    byomukesh.setEmail("bbakshi@gmail.com");
    byomukesh.setPassword(pwdEncoder.encode(PASSWORD));
    byomukesh.setAbout("This guy is a private Detective");
    byomukesh.getRoles().add(adminUser);

    List<User> users = List.of(johnCena, johnDoe, johnWick, deadPool, byomukesh);
    userRepository.saveAll(users);

  }

  private void loadBlogs() {

    LocalDateTime localDateTime = LocalDateTime.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = localDateTime.format(dateTimeFormatter);
    logger.info("Formatted Date: {}", formattedDate);

    Blog blog1 = new Blog();
    blog1.setTitle("Cricket U19 Champions");
    blog1.setContent("The first month of 2023 has ended with a bang with India winning the ICC U19 Women Cricket World Cup.");
    blog1.setCategory(sports);
    blog1.setUser(byomukesh);
    blog1.setBloggedDate(formattedDate);

    Blog blog2 = new Blog();
    blog2.setTitle("Intel ARC! is getting better?");
    blog2.setContent("With it's first entry in the discrete GPU market, the intel GPUs were not performant as the user were expecting them to. With the new driver update it is now running DX12 games better and the DX9 games are now perform much better than earlier.");
    blog2.setCategory(tech);
    blog2.setUser(johnDoe);
    blog2.setBloggedDate(formattedDate);

    Blog blog3 = new Blog();
    blog3.setTitle("Pathan");
    blog3.setContent("SRK's awaited movie Pathan has been release on 16th of Jan,2023 and is has broke all the previous day one Box Office collection records");
    blog3.setCategory(movies);
    blog3.setUser(byomukesh);
    blog3.setBloggedDate(formattedDate);

    Blog blog4 = new Blog();
    blog4.setTitle("Ronaldo, there is still a fight");
    blog4.setContent("After being dropped from his last club Real Madrid, Ronaldo as accepted the offer from the Dubai's football club and in his first game Ronaldo has performed better than what was expected");
    blog4.setCategory(sports);
    blog4.setUser(johnCena);
    blog4.setBloggedDate(formattedDate);

    Blog blog5 = new Blog();
    blog5.setTitle("The OceanCleanUp3");
    blog5.setContent("The first month of 2023 has ended with a bang with India winning the ICC U19 Women Cricket World Cup.");
    blog5.setCategory(tech);
    blog5.setUser(byomukesh);
    blog5.setBloggedDate(formattedDate);

    Blog blog6 = new Blog();
    blog6.setTitle("The OceanCleanUp3");
    blog6.setContent("The CEO and the founder of the OceanCleanUp company has just revealed the final prototype of the OceanCleanUp3 system");
    blog6.setCategory(tech);
    blog6.setUser(deadPool);
    blog6.setBloggedDate(formattedDate);

    Blog blog7 = new Blog();
    blog7.setTitle("Avatar 2, 13 years in making");
    blog7.setContent(LOREM_STRING);
    blog7.setCategory(movies);
    blog7.setUser(johnWick);
    blog7.setBloggedDate(formattedDate);

    Blog blog8 = new Blog();
    blog8.setTitle("Volleyball");
    blog8.setContent(LOREM_STRING);
    blog8.setCategory(sports);
    blog8.setUser(johnCena);
    blog8.setBloggedDate(formattedDate);

    Blog blog9 = new Blog();
    blog9.setTitle("Hundai & the future of ICE");
    blog9.setContent(LOREM_STRING);
    blog9.setCategory(tech);
    blog9.setUser(byomukesh);
    blog9.setBloggedDate(formattedDate);

    Blog blog10 = new Blog();
    blog10.setTitle("Wakanda2");
    blog10.setContent(LOREM_STRING);
    blog10.setCategory(tech);
    blog10.setUser(johnCena);
    blog10.setBloggedDate(formattedDate);

    Blog blog11 = new Blog();
    blog11.setTitle("The Two Sons");
    blog11.setContent(LOREM_STRING);
    blog11.setCategory(novels);
    blog11.setUser(byomukesh);
    blog11.setBloggedDate(formattedDate);

    Blog blog12 = new Blog();
    blog12.setTitle("The Green Book");
    blog12.setContent(LOREM_STRING);
    blog12.setCategory(movies);
    blog12.setUser(johnWick);
    blog12.setBloggedDate(formattedDate);

    List<Blog> blogs = List.of(blog1, blog2, blog3, blog4, blog5, blog6, blog7, blog8, blog9, blog10, blog11, blog12);

    blogRepository.saveAll(blogs);
  }

}
