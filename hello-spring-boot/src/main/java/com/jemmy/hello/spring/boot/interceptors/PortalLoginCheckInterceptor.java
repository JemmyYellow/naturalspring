package com.jemmy.hello.spring.boot.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.LoginUserMapper;
import com.jemmy.hello.spring.boot.pojo.LoginUser;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;


@Component
public class PortalLoginCheckInterceptor implements HandlerInterceptor {
    /**在请求到达controller之前
     *
     * @param request
     * @param response
     * @param handler
     * @return true:可以通过拦截器 false:拦截请求
     * @throws Exception
     */

    @Autowired
    LoginUserMapper loginUserMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user != null){
//            return true;
//        }
        List<LoginUser> list = loginUserMapper.selectByStatus(0);
        if(list != null && list.size()>0){
            return true;
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
