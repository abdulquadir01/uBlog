package dev.aq.ublog.constants;

public class UBlogConstants {

  private UBlogConstants() {
    throw new IllegalStateException("Constant class can't be instantiated.");
  }

  public static final String DEFAULT_PAGE_NUMBER = "0";
  public static final String DEFAULT_PAGE_SIZE = "50";
  public static final String DEFAULT_SORT_BY_FIELD = "blogId";
  public static final String DEFAULT_SORT_DIRECTION = "asc";
  public static final String REVERSE_SORT_DIRECTION = "desc";
  public static final String ID = "id";
  public static final String USER_ID = "userId";
  public static final String BLOG_ID = "blogId";
  public static final String CATEGORY_ID = "categoryId";
  public static final String COMMENT_ID = "commentId";
  public static final String EMAIL = "email";
  //    JWT constants
  public static final Long JWT_TOKEN_VALIDITY = (long) (5 * 60 * 60);

  //    Role constants
  public static final Integer ROLE_ADMIN_CODE = 502;
  public static final Integer ROLE_NORMAL_CODE = 501;

  public static final String ROLE_ADMIN_USER = "ADMIN_USER";
  public static final String ROLE_NORMAL_USER = "NORMAL_USER";

  public static final String CONTENT = """
      Lorem ipsum is placeholder text commonly used in the graphic, print
      and publishing industries for previewing layouts and visual mockups.""";

  public static final String LOREM_STRING = "Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries for previewing layouts and visual mockups.";
  public static final String PASSWORD = "p@ssW0rd";
}
