package me.liuwenhan.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by wsk1103 on 2017/10/18.
 */
@Aspect
public class OnlyOneUser {
    @Before(value = "execution(* me.liuwenhan.controller.UserController.login(..))")
    public void isExit(){

    }
}
