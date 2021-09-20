package com.fjace.pay.demo.controller.img;

import com.fjace.pay.core.util.SignUtils;
import com.fjace.pay.demo.controller.pay.AbstractPayOrderController;
import com.fjace.pay.util.CodeImagesUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 扫描图片生成接口（二维码、条形码）
 * @date 2021-09-10 20:24:00
 */
@RestController
@RequestMapping("/api/scan")
public class ScanImgController extends AbstractPayOrderController {

    /**
     * 返回 图片地址信息
     **/
    @RequestMapping("/images/{aesStr}.png")
    public void qrCodeImages(@PathVariable("aesStr") String aesStr) throws Exception {
        String str = SignUtils.aesDecode(aesStr);
        int width = getValIntegerDefault("width", 200);
        int height = getValIntegerDefault("height", 200);
        CodeImagesUtils.writeQrCode(response.getOutputStream(), str, width, height);
    }
}
