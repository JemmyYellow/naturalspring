package com.jemmy.hello.spring.boot.service.impl;

import com.jemmy.hello.spring.boot.common.Const;
import com.jemmy.hello.spring.boot.common.ResponseCode;
import com.jemmy.hello.spring.boot.dao.LoginUserMapper;
import com.jemmy.hello.spring.boot.dao.UserMapper;
import com.jemmy.hello.spring.boot.pojo.LoginUser;
import com.jemmy.hello.spring.boot.pojo.User;
import com.jemmy.hello.spring.boot.service.IUserService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginUserMapper loginUserMapper;

    /**
     * 登录逻辑
     *
     * @param username 用户名
     * @param password 密码
     * @return ServerResponse
     */
    @Override
    public ServerResponse loginLogic(String username, String password) {

        //step1.用户名和密码的非空判断
        if (null == username || username.equals("")) {
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EMPTY.getCode(),
                    ResponseCode.USERNAME_EMPTY.getMsg());
        }
        if (null == password || password.equals("")) {
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_EMPTY.getCode(),
                    ResponseCode.PASSWORD_EMPTY.getMsg());
        }

        //step2.查看用户名是否存在
        User user1 = userMapper.findByUsername(username);
        if (null == user1) {
            //用户名不存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }

        //step3.根据用户名和密码查询
        User user = userMapper.findByUsernameAndPassword(username, password);
        if (user == null) {
            //密码错误
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getMsg());
        }

        //step4.返回结果
        //LoginUser增加
        int signOut = loginUserMapper.allSignOut();
        if (signOut <= 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.LOGIN_FAIL.getCode(),
                    ResponseCode.LOGIN_FAIL.getMsg());
        }
        LoginUser loginUser = loginUserMapper.selectByUsername(username);
        if (loginUser == null) {
            int insert = loginUserMapper.putLoginUser(username);
            if (insert == 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.LOGIN_FAIL.getCode(),
                        ResponseCode.LOGIN_FAIL.getMsg());
            }
        } else {
            loginUser.setStatus(0);
            int up = loginUserMapper.updateByPrimaryKey(loginUser);
            if (up == 0) {
                return ServerResponse.createServerResponseByFail(ResponseCode.LOGIN_FAIL.getCode(),
                        ResponseCode.LOGIN_FAIL.getMsg());
            }
        }
        return ServerResponse.createServerResponseBySuccess(user);
    }

    /**
     * 注册逻辑
     *
     * @param user 用户
     * @return ServerResponse
     */
    @Override
    public ServerResponse registerLogic(User user) {
        if (user == null) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }

        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();
        user.setRole(Const.NORMAL_USER);

        //1.判断参数是否为空
        if (StringUtils.isBlank(username)) {
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EMPTY.getCode(),
                    ResponseCode.USERNAME_EMPTY.getMsg());
        }
        if (null == password || password.equals("")) {
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_EMPTY.getCode(),
                    ResponseCode.PASSWORD_EMPTY.getMsg());
        }
        if (null == phone || phone.equals("")) {
            //
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_EMPTY.getCode(),
                    ResponseCode.PHONE_EMPTY.getMsg());
        }

        //2.判断用户名是否存在
        User user1 = userMapper.findByUsername(username);
        if (null != user1) {//用户名存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),
                    ResponseCode.USERNAME_EXISTS.getMsg());
        }

        //3.判断手机号是否存在
        Integer i2 = userMapper.findByPhone(phone);
        if (i2 != null && i2 > 0) {//手机存在
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_EXISTS.getCode(),
                    ResponseCode.PHONE_EXISTS.getMsg());
        }

        //4.注册  (加密？)
        int result = userMapper.insert(user);
        if (result == 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),
                    ResponseCode.REGISTER_FAIL.getMsg());
        }

        //注册成功
        return ServerResponse.createServerResponseBySuccess();
    }

    @Override
    public ServerResponse updateUserLogic(User user) {

        return null;
    }

    /**
     * 注销
     *
     * @param username 用户名
     * @return ServerResponse
     */
    @Override
    public ServerResponse signOut(String username) {
        int result = loginUserMapper.signOutByUsername(username);
        if (result == 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.SIGN_OUT_FAIL.getCode(),
                    ResponseCode.SIGN_OUT_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 更改手机
     * @param userId 用户id
     * @param newphone 新手机号
     * @return ServerResponse
     */
    @Override
    public ServerResponse changePhone(Integer userId, String newphone) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERID_NOT_EXISTS.getCode(),
                    ResponseCode.USERID_NOT_EXISTS.getMsg());
        }
        if(user.getPhone().equals(newphone)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_IS_SAME.getCode(),
                    ResponseCode.PHONE_IS_SAME.getMsg());
        }
        int result = userMapper.changePhoneById(userId, newphone);
        if(result <= 0){//改不了？
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_CHANGE_FAIL.getCode(),
                    ResponseCode.PHONE_CHANGE_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }

    /**
     * 更改密码
     * @param id 用户id
     * @param old 旧密码
     * @param now 新密码
     * @return ServerResponse
     */
    @Override
    public ServerResponse changepwdLogic(Integer id, String old, String now) {

        //判空
        if (StringUtils.isBlank(old) || StringUtils.isBlank(now)) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_EMPTY.getCode(),
                    ResponseCode.PARAMETER_EMPTY.getMsg());
        }

        //若原密码不对
        boolean same = userMapper.selectByPrimaryKey(id).getPassword().equals(old);
        if (!same) {
            return ServerResponse.createServerResponseByFail(ResponseCode.OLD_PASSWORD_WRONG.getCode(),
                    ResponseCode.OLD_PASSWORD_WRONG.getMsg());
        }

        //若新旧密码一样
        if (old.equals(now)) {
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_IS_SAME.getCode(),
                    ResponseCode.PASSWORD_IS_SAME.getMsg());
        }

        //改密码
        int result = userMapper.changePasswordById(id, now);
        if (result <= 0) {  //改不了？
            return ServerResponse.createServerResponseByFail(ResponseCode.CHANGE_PASSWORD_FAIL.getCode(),
                    ResponseCode.CHANGE_PASSWORD_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess();
    }
}
