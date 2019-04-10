package com.easipass.business.captcha;

import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.service.impl.RedisUtil;
import com.easipass.sys.util.ApiResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.patchca.color.ColorFactory;
import org.patchca.filter.predefined.*;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class CaptchaController {
    protected final Log log = LogFactory.getLog(this.getClass());

    private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
    private static Random random = new Random();
    static {
        cs.setColorFactory(new ColorFactory() {
            @Override
            public Color getColor(int x) {
                int[] c = new int[3];
                int i = random.nextInt(c.length);
                for (int fi = 0; fi < c.length; fi++) {
                    if (fi == i) {
                        c[fi] = random.nextInt(71);
                    } else {
                        c[fi] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
        RandomWordFactory wf = new RandomWordFactory();
        wf.setCharacters("123456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        cs.setWordFactory(wf);
    }

    @RequestMapping("/pcruuid")
    @ResponseBody
    public ApiResult pcruuid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = cs.getCaptcha().getChallenge();
        log.info("当前的验证码=" + token);
        String UUID = java.util.UUID.randomUUID().toString().replace("-","");
        RedisUtil.setEx("Captcha_"+UUID, token,5*60);
        Map map = new HashMap();
        map.put("captchaUuid",UUID);
        return ApiResult.newInstance(SysConstants.FLAG_T,"","",map);
    }

    @RequestMapping("/pcrimg/{captchaUuid}")
    public void crimg(@PathVariable String captchaUuid , HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (random.nextInt(5)) {
            case 0:
                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
        setResponseHeaders(response);
        log.info("当前的验证UUID=" + captchaUuid);
        String token = RedisUtil.get("Captcha_"+captchaUuid);
        if(null == token){
            log.error("token已过期");
        }else{
            BufferedImage bufImage = new BufferedImage(160, 70, BufferedImage.TYPE_INT_ARGB);
            cs.getBackgroundFactory().fillBackground(bufImage);
            cs.getTextRenderer().draw(token, bufImage, cs.getFontFactory(), cs.getColorFactory());
            bufImage = cs.getFilterFactory().applyFilters(bufImage);
            Captcha captcha = new Captcha(token, bufImage);
            ImageIO.write(captcha.getImage(), "png", response.getOutputStream());
        }
    }

    protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }
}
