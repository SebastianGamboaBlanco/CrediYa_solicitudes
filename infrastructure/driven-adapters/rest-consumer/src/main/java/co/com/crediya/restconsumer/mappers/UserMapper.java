package co.com.crediya.restconsumer.mappers;

import co.com.crediya.model.User;
import co.com.crediya.restconsumer.entities.UserResponse;

public class UserMapper {
    
    public static User toDomain(UserResponse response) {
        return new User(response.getUser().getEmail());
    }
}