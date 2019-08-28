package com.nandu.taike.controller.scSysLogInfo.login;

import com.alibaba.druid.support.json.JSONUtils;
import com.nandu.taike.rabbitMq.Producer;
import com.nandu.taike.util.CookieUtil;
import com.nandu.taike.util.redis.RedisUtil;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/loginIn")
public class UserLoginIn {

    private static final Logger log = LoggerFactory.getLogger(UserLoginIn.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Producer producer;
    @Autowired
    private CookieUtil cookieUtil;

    @PostMapping("/loginIn")
    public String loginIn(
            String username,String password,
            HttpServletRequest request,HttpServletResponse response){
        //登录成功,生成token
        String token = UUID.randomUUID().toString().replace("-","");
        redisUtil.sSet("REDIS_SESSION_KEY"+":"+token,JSONUtils.toJSONString("Hello"));
        //设置session过期时间
        redisUtil.expire("REDIS_SESSION_KEY"+":"+token,3000);
        //写cookie
        cookieUtil.setCookie(request,response,"TK_TOKEN",token);
        producer.send();
        return null;
    }
    /***
     **通过token拿到用户信息
     **Service接收token，根据token查询redis，
     **查询到结果返回用户对象，更新过期时间。
     **如果查询不到结果，返回Session已经过期，状态码400。
     ****/
    @PostMapping("/getUserByToken")
    public Object getUserByToken(String token){
        //根据token获取用户信息
        Set<Object> jsob = redisUtil.sGet("REDIS_SESSION_KEY"+":"+token);
        if (StringUtils.isEmpty(jsob)&&jsob.size() == 0){
            return "用户Session已过期";
        }
        List<Object> tokenList = new ArrayList<>(jsob);
        JSONUtils.parse(tokenList.get(0).toString());
        redisUtil.expire("REDIS_SESSION_KEY"+":"+token,400);
        return  null;
    }
    /***
     * *通过token拿到用户信息
     ***/
    /*
        @PostMapping("/getUserByToken")
        public Object getUserByToken(@PathVariable String token, String callback){
            if (!StringUtils.isEmpty(callback)){
    //            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue()
            }
            return null;
        }
    */
}
