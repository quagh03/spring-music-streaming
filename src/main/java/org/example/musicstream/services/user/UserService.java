package org.example.musicstream.services.user;

import org.example.musicstream.dtos.requests.user.UserCreationRequest;
import org.example.musicstream.dtos.response.api.PageResponse;
import org.example.musicstream.dtos.response.user.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest user);

    UserResponse getUserById(Long id);

    PageResponse<?> getAllUsersWithSortBy(int pageNumber, int pageSize, String sortBy);

    PageResponse<?> getAllUsersWithSortByMultColumn(int pageNumber, int pageSize, String... sortBy);

    boolean changeUserStatus(Long id);
}
