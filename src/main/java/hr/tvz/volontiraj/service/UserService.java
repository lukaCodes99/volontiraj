    package hr.tvz.volontiraj.service;

    import hr.tvz.volontiraj.dto.UserDto;
    import hr.tvz.volontiraj.model.UserEntity;

    import java.util.List;

    public interface UserService {


        public UserDto findById(Long id);

        public UserDto save(UserEntity user);

        public UserDto update(Long id, UserEntity user);

        public void deleteById(Long id);

        public UserEntity findByEmail(String email);

        public String currentUserEmail();

        public List<String> getAllEmailsOfVolunteersForHour();

    }
