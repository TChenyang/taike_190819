package com.nandu.taike.service.login.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginInServiceImpl {

    public String login(String username,String password){

        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        //获取用户
        Map<String,Object> params = new HashMap<>();
        params.put("username",username);
        List<Object> userObject = new ArrayList<>();
        userObject.add(new Object());
        userObject.add(new Object());
        if (userObject.size() > 1){
            return "RETURN";
        }
        //token
        Serializable id = subject.getSession().getId();

        return "";
    }

}
