package com.easipass.business.controller;

import com.easipass.annotation.EntityClass;
import com.easipass.business.entity.GccGdwsConfig;
import com.easipass.sys.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LH on 2017/5/18.
 */
@RestController
@RequestMapping("/gdwsConfig")
@EntityClass(GccGdwsConfig.class)
public class GccGdwsConfigController extends BaseController {


}
