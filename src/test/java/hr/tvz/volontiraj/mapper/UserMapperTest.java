package hr.tvz.volontiraj.mapper;

import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.model.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    void mapUserDtoToUser(){
        UserDto userDto = new UserDto();
        userDto.setId(123L);
        userDto.setUsername("username");
        userDto.setName("name");
        userDto.setSurname("surname");
        userDto.setEmail("email@email.com");
        userDto.setBio("biooooo....");

        UserEntity userEntity = UserMapper.mapUserDtoToUser(userDto);

        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getUsername(), userEntity.getUsername());
        assertEquals(userDto.getName(), userEntity.getName());
        assertEquals(userDto.getSurname(), userEntity.getSurname());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getBio(), userEntity.getBio());
    }

}
