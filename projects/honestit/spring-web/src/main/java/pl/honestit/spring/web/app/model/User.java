package pl.honestit.spring.web.app.model;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String country;
}
