package com.fjace.pay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author fjace
 * @version 1.0.0
 * @Description 商户信息表
 * @date 2021-09-05 23:18:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_mch_info")
public class MchInfo {

    public static LambdaQueryWrapper<MchInfo> gw() {
        return new LambdaQueryWrapper<>();
    }

    public static final byte TYPE_NORMAL = 1; //商户类型： 1-普通商户

    /**
     * 商户号
     */
    @TableId(value = "mch_no", type = IdType.INPUT)
    private String mchNo;

    /**
     * 商户名称
     */
    private String mchName;

    /**
     * 商户简称
     */
    private String mchShortName;

    /**
     * 类型: 1-普通商户, 2-特约商户(服务商模式)
     */
    private Byte type;

    /**
     * 服务商号
     */
    private String isvNo;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactTel;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 商户状态: 0-停用, 1-正常
     */
    private Byte state;

    /**
     * 商户备注
     */
    private String remark;

    /**
     * 初始用户ID（创建商户时，允许商户登录的用户）
     */
    private Long initUserId;

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
     * 更新时间
     */
    private Date updatedAt;
}
