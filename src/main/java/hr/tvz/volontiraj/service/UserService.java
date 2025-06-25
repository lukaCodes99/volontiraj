    package hr.tvz.volontiraj.service;

    import hr.tvz.volontiraj.dto.EventDto;
    import hr.tvz.volontiraj.dto.UserDto;
    import hr.tvz.volontiraj.model.UserEntity;
    import org.springframework.data.domain.Pageable;

    import java.util.List;

    import java.util.List;

    public interface UserService {


        public UserDto findById(Long id);

        public UserDto save(UserEntity user);

        public UserDto update(Long id, UserEntity user);

        public void deleteById(Long id);

        public UserEntity findByEmail(String email);

        public String currentUserEmail();

        public List<EventDto> getUserVolunteerHistory(Long id);

        public List<String> getAllEmailsOfVolunteersForHour();


        public UserEntity getCurrentUser();
    }
