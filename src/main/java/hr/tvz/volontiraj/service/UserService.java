package hr.tvz.volontiraj.service;

import hr.tvz.volontiraj.dto.UserDto;
import hr.tvz.volontiraj.model.UserEntity;

public interface UserService {


    public UserDto findById(Long id);

    public UserDto save(UserEntity user);

    public UserDto update(Long id, UserEntity user);

    public void deleteById(Long id);
}
