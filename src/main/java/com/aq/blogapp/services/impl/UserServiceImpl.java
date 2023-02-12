package com.aq.blogapp.services.impl;

import com.aq.blogapp.constants.AppConstants;
import com.aq.blogapp.exceptions.ResourceNotFoundException;
import com.aq.blogapp.model.Blog;
import com.aq.blogapp.model.Role;
import com.aq.blogapp.model.User;
import com.aq.blogapp.payload.DTO.BlogDTO;
import com.aq.blogapp.payload.DTO.UserDTO;
import com.aq.blogapp.payload.response.BlogsResponse;
import com.aq.blogapp.payload.response.UsersResponse;
import com.aq.blogapp.respositories.RoleRepository;
import com.aq.blogapp.respositories.UserRepository;
import com.aq.blogapp.services.UserService;
import com.aq.blogapp.utils.AppUtils;
import com.aq.blogapp.utils.mappers.UserMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public UsersResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Pageable pageable = createSortedPageable(pageNumber, pageSize, sortBy, sortDir);

        Page<User> userPage = userRepository.findAll(pageable);

        return createUserResponse(userPage);
    }


    @Override
    public UserDTO getUserById(Long id) {
        UserDTO userDTOById = new UserDTO();

        try {
            if (id != null) {
                userDTOById = userRepository
                        .findById(id)
                        .map(userMapper::userToUserDto)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
            }
        } catch (NoSuchElementException ex) {
            throw new ResourceNotFoundException("User", "userId", id);
        }

        return userDTOById;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserDTO userByEmail = new UserDTO();

        try {
            if (email != null) {
                userByEmail = userRepository
                        .findByEmail(email)
                        .map(userMapper::userToUserDto)
                        .orElseThrow(() -> new ResourceNotFoundException("User", email));
            }
        } catch (NoSuchElementException ex) {
            throw new ResourceNotFoundException("User", email);
        }

        return userByEmail;
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserDTO newUserDTO = new UserDTO();


        if (!AppUtils.anyEmpty(userDTO)) {
            newUserDTO = saveAndReturnDTO(userMapper.userDtoToUser(userDTO));
        }

        return newUserDTO;
    }


    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserDTO updatedUser = new UserDTO();
        User existingUser = new User();
        try {
            existingUser = userRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPassword(userDTO.getPassword());
            existingUser.setAbout(userDTO.getAbout());

            updatedUser = userMapper.userToUserDto(userRepository.save(existingUser));

        } catch (ResourceNotFoundException RNFE) {
            RNFE.getMessage();
        }

        return updatedUser;
    }


    @Override
    public void deleteUser(Long id) {

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ERDAE) {
            System.out.println(ERDAE.getMessage());
            System.out.println(ERDAE.getCause());
            throw new ResourceNotFoundException("User", "id", id);
        }

    }


    @Override
    public UserDTO registerUser(UserDTO userDTO) {

        User newUser = userMapper.userDtoToUser(userDTO);

//      encoding the password
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

//        roles
        Role role = roleRepository.findById(AppConstants.ROLE_NORMAL_CODE).get();
        System.out.println("find role by id: " + role.toString());
        newUser.getRoles().add(role);

        User registeredUser = userRepository.save(newUser);

        return userMapper.userToUserDto(registeredUser);
    }


    //  PRIVATE methods
    private UserDTO saveAndReturnDTO(User user) {
        UserDTO returnedDto = new UserDTO();
        User savedUser = new User();

        if (user != null) {
            savedUser = userRepository.save(user);
            returnedDto = userMapper.userToUserDto(savedUser);
        }

        return returnedDto;
    }



    //  ==================================================================
//    PRIVATE METHODS

    /**
     * return the blogResponse class with proper properties.
     * @param blogPage
     * @return
     */


    private UsersResponse createUserResponse(Page<User> userPage) {

        UsersResponse usersResponse = new UsersResponse();

        List<UserDTO> users;
        users = userPage
                .getContent()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());

        usersResponse.setUsers(users);
        usersResponse.setPageNumber(userPage.getNumber());
        usersResponse.setPageSize(userPage.getSize());
        usersResponse.setTotalPages(userPage.getTotalPages());
        usersResponse.setTotalElements(userPage.getTotalElements());
        usersResponse.setLastPage(userPage.isLast());

        return usersResponse;
    }

    /**
     * this method return a sorted pageable
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    private Pageable createSortedPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

//      Be cautious of this statement
//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
        Sort sort = null;

        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending();
        } else if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        }

//      !! CAUTION !! TBD - find a way to initialize sort with some other value than null
        return PageRequest.of(pageNumber, pageSize, sort);
    }


//EoC - End of Class


}
