package com.jemmy.hello.spring.boot.Controller;

import com.jemmy.hello.spring.boot.common.Const;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.pojo.User;
import com.jemmy.hello.spring.boot.service.IUserService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user/")
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "login.do")
    public ServerResponse login(String username, String password, HttpSession session) {

        ServerResponse serverResponse = userService.loginLogic(username, password);
        //判断是否已经登录
        if (serverResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "register.do")
    public ServerResponse<User> register(User user) {
        return userService.registerLogic(user);
    }

    @RequestMapping(value = "changepwd.do")
    public ServerResponse changePassword(Integer userid, String old, String now) {

        return userService.changepwdLogic(userid, old, now);
    }

    @RequestMapping(value = "signOut.do")
    public ServerResponse signOut(String username) {
        return userService.signOut(username);
    }

    @RequestMapping(value = "update.do")
    public ServerResponse updateUser(User user, HttpSession session) {

        //判断用户是否登录
        User loginedUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (loginedUser == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getMsg());
        }
        if (user == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }

        return userService.updateUserLogic(user);
    }

    @RequestMapping(value = "changephone.do")
    public ServerResponse changePhone(@RequestParam(value = "userid") Integer userId, String newphone){
        return userService.changePhone(userId, newphone);
    }

}
