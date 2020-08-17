package com.tradingbot.kuna.service.user;

import com.tradingbot.kuna.model.User;
import com.tradingbot.kuna.model.UserState;
import com.tradingbot.kuna.repository.UserRepository;
import com.tradingbot.kuna.utils.CommonUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void changeState(User user, UserState userState) {
        user.setUserState(userState);
        save(user);
    }

    @Transactional
    public User findById(Long userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    public boolean isUserExist(Long id) {
        return userRepository.findByUserId(id).isPresent();
    }

    @Transactional
    public void registerNewUser(String firstName, String lastName, String userName, Long id) {
        Instant dateRegistered = CommonUtils.getNowTimeInUtc();
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setDateRegistered(dateRegistered);
        user.setUserState(UserState.STARTED);
        save(user);
    }

}
