package com.nandu.taike.util;

import com.nandu.taike.pojo.scSysLogInfo.TkSysLogInfo;
import com.nandu.taike.service.login.LoginInService;
import com.nandu.taike.service.scSysLogInfo.TkSysLogInfoService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
@Aspect
@Transactional(readOnly = true)
public class SysLogAspect {

    private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private TkSysLogInfoService tkSysLogInfoService;

    //com.kzj.kzj_rabbitmq.controller 包中所有的类的所有方法切面
    //@Pointcut("execution(public * com.kzj.kzj_rabbitmq.controller.*.*(..))")
    //只针对 MessageController 类切面
    //@Pointcut("execution(public * com.kzj.kzj_rabbitmq.controller.MessageController.*(..))")
    //统一切点,对com.kzj.kzj_rabbitmq.controller及其子包中所有的类的所有方法切面
    @Pointcut("execution(public * com.nandu.taike.controller.scSysLogInfo.TkSysLogInfoController.*(..))")
    public void pointCut(){

    }

    //前置通知
    @Before("pointCut()")
    public void beforeMethod(JoinPoint joinPoint){

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //得到请求资源
        String requestUri = request.getRequestURI();
        //得到来访者IP地址
        String remoteAddr = request.getRemoteAddr();
        //得到请求URL地址时使用的方法
        String method = request.getMethod();
        //客户端主机名
        String remoteHost = request.getRemoteHost();

        String className  = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();

        log.info("+++切入点开始切入内容+++");
        log.info("requestUri=" + requestUri + ",remoteAddr=" + remoteAddr
                + ",method=" + method + ",remoteHost=" + remoteHost + ",className=" + className);

        TkSysLogInfo logInfo = new TkSysLogInfo();
        logInfo.setTkId(UUID.randomUUID().toString().replace("-",""));
        logInfo.setTkUserName("Hi Hi im TK");
        logInfo.setTkIp(remoteAddr);
        logInfo.setTkRemoteHost(remoteHost);
        logInfo.setTkClassName(className);
        logInfo.setTkUrl(requestUri);
        logInfo.setTkMethod(method);
        boolean isSaveSysLog = tkSysLogInfoService.save(logInfo);
        if (isSaveSysLog){
            log.info("日志记录添加成功");
        }
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinPoint){
        log.info("------->在切入点结尾处(方法执行后)切入内容...");
    }

    @AfterReturning(returning = "result",pointcut = "pointCut()")
    public void doAfterReturning(Object result){
        log.info("切入点return内容之后切入内容："+result);
    }

}
