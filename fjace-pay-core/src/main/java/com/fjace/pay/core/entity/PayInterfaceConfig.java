package com.fjace.pay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fjace.pay.core.model.BasicModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 支付接口配置
 * @date 2021-09-05 17:28:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_pay_interface_config")
public class PayInterfaceConfig extends BasicModel<PayInterfaceConfig> implements Serializable {
    private static final long serialVersionUID = -9097021004646225331L;

    public static LambdaQueryWrapper<PayInterfaceConfig> gw() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号类型:1-服务商 2-商户
     */
    private Byte infoType;

    /**
     * 服务商或商户No
     */
    private String infoId;

    /**
     * 支付接口代码
     */
    private String ifCode;

    /**
     * 接口配置参数,json字符串
     */
    private String ifParams;

    /**
     * 支付接口费率
     */
    private BigDecimal ifRate;

    /**
     * 状态: 0-停用, 1-启用
     */
    private Byte state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者用户ID
     */
    private Long createdUid;

    /**
     * 创建者姓名
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新者用户ID
     */
    private Long updatedUid;

    /**
     * 更新者姓名
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedAt;
}
