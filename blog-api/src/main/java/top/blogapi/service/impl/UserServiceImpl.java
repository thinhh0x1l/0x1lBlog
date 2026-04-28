package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.User;
import top.blogapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Người đùng không tồn tại");
        }
        return user;
    }
}
