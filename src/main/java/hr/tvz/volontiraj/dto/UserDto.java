package hr.tvz.volontiraj.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String profilePicturePath; //tu cemo dodati neke urlove u bazu koji su besplatno dostupni online kako bi imali konzistentan prikaz
    private String username;
    private String name;
    private String surname;
    private String email;
    private String bio;
}
