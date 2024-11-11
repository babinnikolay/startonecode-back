package ru.hukola.startonecodeback.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User getSecurityUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findUserByName(authentication.getName());
    }

    public User findUserByName(String name) {
        return userRepository.findByNameIgnoreCase(name);
    }

//    public User createUser(UserRegistrationDto dto) {
//        User user = new User();
//        user.setName(dto.getName());
//        user.setEmail(dto.getEmail());
//        user.setUuid(UUID.randomUUID());
//        user.setPassword(encoder.encode(dto.getPassword()));
//        return this.userRepository.save(user);
//    }
//
//    public NewUserCredentialDto changeEmail(NewUserCredentialDto dto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = findUserByName(authentication.getName());
//        user.setEmail(dto.getEmail());
//        this.userRepository.save(user);
//        return new NewUserCredentialDto(user.getEmail(), "", "");
//    }
//
//    public NewUserCredentialDto changePassword(NewUserCredentialDto dto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = findUserByName(authentication.getName());
//        if (!encoder.matches(dto.getCurrentPassword(), user.getPassword()))
//            throw new BadCredentialsException("Wrong password");
//
//        user.setPassword(encoder.encode(dto.getNewPassword()));
//        this.userRepository.save(user);
//        return new NewUserCredentialDto("", "", "");
//    }
}
