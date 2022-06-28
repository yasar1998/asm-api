package com.example.ASMapi.service;

import com.example.ASMapi.dto.AppUserDto;
import com.example.ASMapi.dto.IdResponse;

import java.util.List;

public interface AdminService {

    public IdResponse deleteStudentRecord(Long id);

    public List<AppUserDto> getAllStudentRecords();

    public List<AppUserDto> getAllRecords();

    public AppUserDto getStudentRecordById(Long id);
}
