package com.itcast.myweb.controller.entity;


import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.entity.User;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tianmao/entity/users")
@Slf4j
public class UserController {// 用户控制


    @Autowired
    private UserService userService;// 用户服务


    //创建用户
    @PostMapping("/create")
    public Result createUser(@RequestBody User user) {

        //创建用户
        log.info("创建用户：{}", user);

        //调用service
        userService.createUser(user);


        return Result.ok();

    }

    //获取用户
    @GetMapping("/get")
    public Result getUser(@RequestParam Long id) {

        //获取用户
        log.info("获取用户，用户id={}", id);

        //调用service
        UserDTO userDTO = userService.getUser(id);

        //判断用户是否存在
        if (userDTO == null) {
            return Result.error("用户不存在");
        }


        return Result.ok(userDTO);

    }


    //修改用户信息
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {

        //修改用户信息
        log.info("修改用户信息：{}", user);

        //调用service
        userService.updateUser(user);


        return Result.ok();

    }


    //注销用户
    @DeleteMapping("/logoff/{id}")
    public Result logoff(@PathVariable Long id) {

        //注销用户
        log.info("注销用户，用户id={}", id);

        //调用service
        userService.logoff(id);


        return Result.ok();

    }





}
