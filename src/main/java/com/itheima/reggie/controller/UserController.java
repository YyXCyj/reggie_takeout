package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author yy
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            session.setAttribute(phone,code);
            return R.success("成功发送");
        }
        return R.error("失败发送");
//            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            log.info(code);
//            session.setAttribute("phone",code);
//            return R.success("成功发送");
    }
    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpSession session){
//        if(user.getPhone()=="18629818528"){
//            return R.success("登陆成功");
//        }
//        return R.error("登陆失败");
        log.info(user.getPhone());
        return R.error("登陆失败");
    }
}
