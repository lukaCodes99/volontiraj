package hr.tvz.volontiraj.service.implementation;

import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.mapper.UserMapper;
import hr.tvz.volontiraj.model.UserEntity;
import hr.tvz.volontiraj.repository.UserRepository;
import hr.tvz.volontiraj.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserDto findById(Long id) {
        return UserMapper.mapUserToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public UserDto save(UserEntity user) {
        return UserMapper.mapUserToUserDto(userRepository.save(user));
    }

    public UserDto update(Long id, UserEntity user) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            UserEntity userEntityToUpdate = userEntity.get();
            userEntityToUpdate.setUsername(user.getUsername());
            userEntityToUpdate.setPassword(user.getPassword());
            userEntityToUpdate.setProfilePicturePath(user.getProfilePicturePath());
            return UserMapper.mapUserToUserDto(userRepository.save(userEntityToUpdate)) ;
        }
        else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
