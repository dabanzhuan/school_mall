package me.liuwenhan.controller;

import me.liuwenhan.pojo.UserInformation;
import me.liuwenhan.service.UserInformationService;
import me.liuwenhan.token.TokenProccessor;
import me.liuwenhan.tool.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Maibenben on 2017/4/30.
 */
@Controller
public class SendEmail {

    @Resource
    private UserInformationService userInformationService;
    private static final Log log = LogFactory.getLog(SendEmail.class);

    //send the Email to the phone
    @RequestMapping(value = "sendCode", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Map sendEmail(HttpServletRequest req, HttpServletResponse res,
                         @RequestParam String phone, @RequestParam String action, @RequestParam String token) {
        log.debug(phone+":开始进入发验证码阶段");
//返回的数据，会被转换成json
        Map<String, String> map = new HashMap<>();
        map.put("result", "-1");
        res.setContentType("text/html;charset=UTF-8");//编码
//token，防止重复提交
        String sendCodeToken = (String) req.getSession().getAttribute("token");
        if (StringUtils.getInstance().isNullOrEmpty(sendCodeToken) || !sendCodeToken.equals(token)) {
            sendCodeToken = TokenProccessor.getInstance().makeToken();
            map.put("token", sendCodeToken);
            return map;
        }
        else {
            req.getSession().removeAttribute("sendCodeToken");
        }
        //判断手机号码是否为正确
        if (!StringUtils.getInstance().isPhone(phone)) {
            sendCodeToken = TokenProccessor.getInstance().makeToken();
            map.put("token", sendCodeToken);
            map.put("result", "0");
            return map;
        }
        //如果是忘记密码提交的发送短信
        if (action.equals("forget")) {
            if (!isUserPhoneExists(phone)) {
                //失败
            map.put("result", "0");
                sendCodeToken = TokenProccessor.getInstance().makeToken();
                map.put("token", sendCodeToken);
                return map;
            }
        } else if (action.equals("register")) {
            //失败
            if (isUserPhoneExists(phone)) {
                sendCodeToken = TokenProccessor.getInstance().makeToken();
                map.put("token", sendCodeToken);
                map.put("result", "-1");
                return map;
            }
        }
        //get the random num to phone which should check the phone to judge the phone is belong user
        getRandomForCodePhone(req);
        String ra = (String) req.getSession().getAttribute("codePhone");
        String text1 = "【LWH的验证码】您的验证码是：";
        String text2 = "，请保护好自己的验证码。";
        String text = text1 + ra + text2;
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.139.com");//????????????smtp????
        prop.setProperty("mail.transport.protocol", "smtp");//???????Э??
        prop.setProperty("mail.smtp.auth", "true");//????????????
        prop.setProperty("mail.smtp.port", "25");//?????25????????????25?????????????
        Session session = Session.getInstance(prop);//?????
        try {
            Transport ts = session.getTransport();//????????
            ts.connect("smtp.139.com", "18331091329@139.com", "1996325Lwh");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("18331091329@139.com"));
            String realPhone = phone;
            phone += "@139.com";
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(phone));
            message.setSubject("来自LWH的验证码");
            message.setContent(text, "text/html;charset=UTF-8");
//            这里先不发生信息，以后要开启的
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            req.getSession().setAttribute("phone", realPhone);
            map.put("result", "1");
        } catch (Exception me) {
            me.printStackTrace();
            sendCodeToken = TokenProccessor.getInstance().makeToken();
            map.put("token", sendCodeToken);
            map.put("result", "1");
        }
        return map;
    }

    // get the random phone`s code 验证码
    private void getRandomForCodePhone(HttpServletRequest req) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        System.out.println(sb.toString());
        req.getSession().setAttribute("codePhone", sb.toString());
    }

//    //检验验证码
    private boolean checkPhoto(String photo, HttpServletRequest request) {
        photo = photo.toLowerCase();
        String true_photo = (String) request.getSession().getAttribute("rand");
        return true_photo.equals(photo);
    }

    //To determine whether the user's mobile phone number exists
    private boolean isUserPhoneExists(String phone) {
        boolean result = false;
        try {
            int id = userInformationService.selectIdByPhone(phone);
            if (id == 0){
                return result;
            }
            UserInformation userInformation = userInformationService.selectByPrimaryKey(id);

            if (StringUtils.getInstance().isNullOrEmpty(userInformation)) {
                return false;
            }
            String userPhone = userInformation.getPhone();
            result = !userPhone.equals("");
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }


}
