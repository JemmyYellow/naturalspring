package com.jemmy.hello.spring.boot.Controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jemmy.hello.spring.boot.service.IPayService;
import com.jemmy.hello.spring.boot.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/portal/pay/")
public class AlipayController {

    @Autowired
    IPayService payService;

    @Value("${alipay.public_key}")
    private String alipayPublicKey;
    /**
     * 支付接口
     */
    @RequestMapping(value = "pay.do")
    public ServerResponse pay(@RequestParam(value = "userid") Integer userId, @RequestParam(value = "orderno") Long orderNo) {

        return payService.pay(userId, orderNo);
    }

    /**
     * 支付宝回调接口
     */
    @RequestMapping(value = "callback.do")
    public String alipayCallback(HttpServletRequest request) {

        Map<String,String[]> params = request.getParameterMap();
        if(params == null || params.size() == 0){
            return "fail";
        }

        Map<String,String> signParam = new HashMap<>();

        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String[] values = params.get(key);
            StringBuffer sb = new StringBuffer();
            for(String value : values){
                sb.append(value).append(",");
            }
            String value1 = sb.toString();
            value1 = value1.substring(0, value1.length()-1);

            signParam.put(key, value1);
        }

        //验证签名
        try {
            signParam.remove("sign_type");
            boolean checkSign = AlipaySignature.rsaCheckV2(signParam, alipayPublicKey, "utf-8", "RSA2");
            if(checkSign){
                //验签通过
                //处理业务逻辑
                System.out.println("验签通过");

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        String success = "success";
        return success;
    }
}
