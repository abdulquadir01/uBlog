//package com.aq.blogapp.configurations;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//
//import java.util.Collections;
//import java.util.List;
//
//
//@Configuration
//@EnableWebMvc
//public class SwaggerConfig {
//
//  public static final String AUTHORIZATION_HEADER = "Authorization";
//
//  @Bean
//  public Docket api() {
//    return new Docket(DocumentationType.SWAGGER_2)
//      .apiInfo(getInfo())
//      .securityContexts(securityContexts())
//      .securitySchemes(List.of(apiKeys()))
//      .select()
//      .apis(RequestHandlerSelectors.any())
//      .paths(PathSelectors.any())
//      .build();
//  }
//
//  private ApiKey apiKeys() {
//    return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
//  }
//
//  private List<SecurityContext> securityContexts() {
//    return List.of(
//      SecurityContext
//        .builder()
//        .securityReferences(securityReferences())
//        .build()
//    );
//  }
//
//  private List<SecurityReference> securityReferences() {
//
//    AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
//    return List.of(new SecurityReference("JWT", new AuthorizationScope[]{scope}));
//  }
//
//  private ApiInfo getInfo() {
//    return new ApiInfo("BlogApp",
//      "Backend of a simple blogging app",
//      "1.0",
//      "TnC", new Contact("Abdul", "https://github.com/abdulquadir01", "abdulquadir01@outlook.com"),
//      "MIT licence 2",
//      "licence URL", Collections.emptyList()
//    );
//  }
//
//}
