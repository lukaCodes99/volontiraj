package hr.tvz.volontiraj.mapper;


import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.model.UserEntity;

public class UserMapper {
    public static UserDto mapUserToUserDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setEmail(user.getEmail());
        userDto.setBio(user.getBio());

        return userDto;
    }

    public static UserEntity mapUserDtoToUser(UserDto dto) {
        UserEntity user = new UserEntity();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setSurname(user.getSurname());
        user.setEmail(user.getEmail());
        user.setBio(user.getBio());

        return user;
    }

}
