package com.fjace.pay.core.exception;

import com.fjace.pay.core.enums.ApiCodeEnum;
import com.fjace.pay.core.model.ApiRes;
import lombok.Getter;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 业务异常
 * @date 2021-09-05 15:42:00
 */
@Getter
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1053940872312390922L;

    private final ApiRes apiRes;

    /**
     * 业务自定义异常
     **/
    public BizException(String msg) {
        super(msg);
        this.apiRes = ApiRes.customFail(msg);
    }

    public BizException(ApiCodeEnum apiCodeEnum, String... params) {
        super();
        apiRes = ApiRes.fail(apiCodeEnum, params);
    }

    public BizException(ApiRes apiRes) {
        super(apiRes.getMsg());
        this.apiRes = apiRes;
    }
}
