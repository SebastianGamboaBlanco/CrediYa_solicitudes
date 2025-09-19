package co.com.crediya.restconsumer.mappers;

import co.com.crediya.model.User;
import co.com.crediya.restconsumer.entities.UserResponse;

public class UserMapper {

    public static User toDomain(UserResponse response) {
        return new User(response.getUser().getEmail());
    }

    public static User toDomainComplete(UserResponse response) {
        var userData = response.getUser();
        return new User(
                userData.getEmail(),
                userData.getFirstName(),
                userData.getLastName(),
                userData.getBaseSalary()
        );
    }
}