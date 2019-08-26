package com.nandu.taike.controller.scSysLogInfo;


import com.nandu.taike.service.login.LoginInService;
import com.nandu.taike.service.scSysLogInfo.TkSysLogInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author TK
 * @since 2019-08-22
 */
@RestController
@RequestMapping("/tkSysLogInfo")
public class TkSysLogInfoController {
    private static final Logger log = LoggerFactory.getLogger(TkSysLogInfoController.class);

    @PostMapping("/toMe")
    public String toMe(){
        log.info(this.getClass().toString());
        return "hello";
    }

}

