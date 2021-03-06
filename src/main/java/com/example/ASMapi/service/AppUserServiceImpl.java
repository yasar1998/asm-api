package com.example.ASMapi.service;

import com.example.ASMapi.dto.AppUserDto;
import com.example.ASMapi.entity.AppUser;
import com.example.ASMapi.exceptions.custom.UnsuccessfulPasswordUpdateException;
import com.example.ASMapi.exceptions.custom.UserNotFoundException;
import com.example.ASMapi.repository.UserRepository;
import com.example.ASMapi.request.PasswordUpdateRequest;
import com.example.ASMapi.request.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUserDto getCurrentRecord(String username) {
        AppUser appUser = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        return modelMapper.map(appUser, AppUserDto.class);
    }

    @Override
    public AppUserDto createRecord(RegisterRequest registerRequest) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        AppUser appUser = userRepository.save(modelMapper.map(registerRequest, AppUser.class));
        return modelMapper.map(registerRequest, AppUserDto.class);
    }

    @Override
    public AppUserDto updatePassword(PasswordUpdateRequest passwordUpdateRequest, String username) {
        AppUser appUser = userRepository.findByUsername(username).get();
        if(passwordEncoder.matches(passwordUpdateRequest.getOldPassword(), appUser.getPassword())){
            appUser.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        }
        else{
            throw new UnsuccessfulPasswordUpdateException("Password cannot be changed");
        }
        userRepository.save(appUser);
        return modelMapper.map(appUser, AppUserDto.class);
    }
}
