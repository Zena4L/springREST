package com.rest.Springrestapi.database;

import com.rest.Springrestapi.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {
    private static int userCount = 0;
    private static final List<User> users = new ArrayList<>();

    static {
        users.add(new User(++userCount,"Clement", LocalDate.now().minusYears(20)));
        users.add(new User(++userCount,"Eve", LocalDate.now().minusYears(10)));
        users.add(new User(++userCount,"Jane", LocalDate.now().minusYears(6)));
    }

    public List<User> findAll(){
        return users;
    }

    public User findOne(int id){
        Predicate<? super User> predicate = user ->  user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
    }

    public User save(User user){
        user.setId(++userCount);
        users.add(user);
        return user;
    }

    public void deleteById(int id){
        Predicate<? super User> predicate = user ->  user.getId().equals(id);
         users.removeIf(predicate);
    }



}
