package org.example.musicstream.services.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.musicstream.dtos.requests.user.UserCreationRequest;
import org.example.musicstream.dtos.response.api.PageResponse;
import org.example.musicstream.dtos.response.user.UserResponse;
import org.example.musicstream.entities.User;
import org.example.musicstream.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for handling user related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    /**
     * Creates a new user.
     * @param user The user creation request object.
     * @return The response object of the created user.
     * @throws IllegalArgumentException if the passwords do not match, or if the username or email is already in use.
     */
    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest user) {
        if(!user.isPasswordMatch()){
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(String.format("Username '%s' has been used!", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException(String.format("Email '%s' has been used!", user.getEmail()));
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .build();
        newUser =  userRepository.save(newUser);

        return UserResponse.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .dateOfBirth(newUser.getDateOfBirth())
                .isActive(newUser.isActive())
                .build();
    }

    /**
     * Retrieves a user by their ID.
     * @param id The ID of the user.
     * @return The response object of the retrieved user.
     * @throws EntityNotFoundException if a user with the given ID does not exist.
     */
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .isActive(user.isActive())
                .build();
    }

    /**
     * Retrieves all users with sorting by a single column.
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @param sortBy The column to sort by.
     * @return A page response object containing the retrieved users.
     * @throws IllegalArgumentException if the page number is less than 0 or the page size is less than 1.
     */
    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNumber, int pageSize, String sortBy){
        validatePageNumberAndSize(pageNumber, pageSize);

        List<Sort.Order> sorts = new ArrayList<>();

        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(Sort.Order.asc(matcher.group(1)));
                }else{
                    sorts.add(Sort.Order.desc(matcher.group(1)));
                }
            }
        }

        return getPageResponse(pageNumber, pageSize, sorts);
    }

    /**
     * Retrieves all users with sorting by multiple columns.
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @param sortBy The columns to sort by.
     * @return A page response object containing the retrieved users.
     */
    @Override
    public PageResponse<?> getAllUsersWithSortByMultColumn(int pageNumber, int pageSize, String... sortBy) {
        validatePageNumberAndSize(pageNumber, pageSize);

        List<Sort.Order> orders = new ArrayList<>();

        if (sortBy != null) {
            for (String s : sortBy) {
                log.info("sortBy: {}", s);
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }

        return getPageResponse(pageNumber, pageSize, orders);
    }

    @Override
    public boolean changeUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d not found", id)));

        user.setActive(!user.isActive());

        user = userRepository.save(user);

        return user.isActive();
    }

    /**
     * Helper method to create a page response object.
     * @param pageNumber The page number.
     * @param pageSize The page size.
     * @param sorts The list of sort orders.
     * @return A page response object.
     */
    private PageResponse<?> getPageResponse(int pageNumber, int pageSize, List<Sort.Order> sorts) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> userResponses =  users.stream().map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .dateOfBirth(user.getDateOfBirth())
                        .isActive(user.isActive())
                        .build()).toList();

        return PageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .items(userResponses)
                .build();
    }

    private void validatePageNumberAndSize(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize < 1) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }
    }
}
