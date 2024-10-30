package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.redis.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
public class MainController {

    @Autowired
    RedisService redisService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String USER_DB_KEY = "epika";

    @PostMapping("/createUser")
    public ResponseDTO<UserDTO> createUser(@RequestBody UserDTO dto) {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        try {
            redisService.addToList("epika", dto);
            responseDTO.setData(dto);
            responseDTO.setMessage("Success");
            responseDTO.setResult(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            responseDTO.setResult(false);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    @GetMapping("/userList")
    public ResponseDTO<List<UserDTO>> userList(UserDTO dto) {
        ResponseDTO<List<UserDTO>> responseDTO = new ResponseDTO<>();
        try {

            List<Object> redisServiceList = redisService.getList("epika");
            List<UserDTO> userDTOList = objectMapper.convertValue(redisServiceList, new TypeReference<List<UserDTO>>() {});
            responseDTO.setData(userDTOList);
            responseDTO.setMessage("Success");
            responseDTO.setResult(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            responseDTO.setResult(false);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    @DeleteMapping("/deleteList")
    public ResponseDTO<UserDTO> deleteList() {
        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();
        try {
            redisService.deleteData("epika");
            responseDTO.setData(null);
            responseDTO.setMessage("Success");
            responseDTO.setResult(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            responseDTO.setResult(false);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
}
