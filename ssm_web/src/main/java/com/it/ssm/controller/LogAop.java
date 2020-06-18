package com.it.ssm.controller;

import com.it.ssm.domain.SysLog;
import com.it.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    private Date visitTime;//开始时间
    private Class clazz;//访问的类
    private Method method;//访问的方法

    //前置通知
    @Before("execution(* com.it.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        visitTime=new Date();//当前时间就是开始时间
        clazz=jp.getTarget().getClass();//具体要访问的类
        String methodName = jp.getSignature().getName();//获取访问的方法的名称
        Object[] args = jp.getArgs();//获取访问的方法的参数

        //获取具体执行的方法的method对象
        if (args==null||args.length==0){
            method = clazz.getMethod(methodName);//智能获取无参的方法
        }else {
            Class[] classArgs = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classArgs[i] = args[i].getClass();
            }
            clazz.getMethod(methodName,classArgs);
        }
    }

    //后置通知
    @After("execution(* com.it.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {

        long time = new Date().getTime()-visitTime.getTime();//获取访问时长

        //获取url
        String url="";
        if (clazz!=null&&method!=null&&clazz!=LogAop.class){
            //1.获取类上的@RequestMapping("/orders")
            RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if (classAnnotation!=null){
                String[] classValue = classAnnotation.value();
                //2.获取方法上的@RequestMapping("findAll.do")
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation!=null){
                    String[] methodValue = methodAnnotation.value();
                    url=classValue[0]+methodValue[0];
                }

            }
            //获取ip
            String ip = request.getRemoteAddr();

            //获取用户
            SecurityContext context = SecurityContextHolder.getContext();
//        request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
            User user = (User) context.getAuthentication().getPrincipal();
            String username = user.getUsername();

            //封装daoSysLog
            SysLog sysLog = new SysLog();
            sysLog.setMethod("[类名] "+clazz.getName() + "[方法名] "+method.getName());
            sysLog.setExecutionTime(time);
            sysLog.setIp(ip);

            sysLog.setUrl(url);
            sysLog.setUsername(username);
            sysLog.setVisitTime(visitTime);

            sysLogService.save(sysLog);
        }









    }

}
