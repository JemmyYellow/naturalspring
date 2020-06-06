package com.jemmy.hello.spring.boot.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.LoginUserMapper;
import com.jemmy.hello.spring.boot.dao.UserMapper;
import com.jemmy.hello.spring.boot.pojo.LoginUser;
import com.jemmy.hello.spring.boot.pojo.User;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Component
public class PortalLoginCheckInterceptor implements HandlerInterceptor {
    /**
     * 在请求到达controller之前
     *
     * @return true:可以通过拦截器 false:拦截请求
     */
    @Autowired
    LoginUserMapper loginUserMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String username = request.getParameter("username");
        if (StringUtils.isNotBlank(username)) {
            LoginUser loginUser = loginUserMapper.selectByUsername(username);
            if (loginUser != null) {
                if (loginUser.getStatus() == 0)
                    return true;
            }
        }

        String userIdStr = request.getParameter("userid");
        if (StringUtils.isNotBlank(userIdStr)) {
            Integer userId = Integer.valueOf(userIdStr);
            User user = userMapper.selectByPrimaryKey(userId);
            if(user != null){
                String username2 = user.getUsername();
                if(StringUtils.isNotBlank(username2)){
                    LoginUser loginUser2 = loginUserMapper.selectByUsername(username2);
                    if (loginUser2 != null) {
                        if (loginUser2.getStatus() == 0)
                            return true;
                    }
                }
            }
        }
        //用户未登录
        response.reset();
        response.addHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        ServerResponse serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),
                ResponseCode.NEED_LOGIN.getMsg());
        ObjectMapper objectMapper = new ObjectMapper();
        String info = objectMapper.writeValueAsString(serverResponse);
        printWriter.write(info);
        printWriter.flush();
        printWriter.close();

        return false;
    }

    //在请求处理完成后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //客户端接收到相应后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
