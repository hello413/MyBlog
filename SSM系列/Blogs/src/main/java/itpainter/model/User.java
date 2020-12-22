package itpainter.model;

import itpainter.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

/**
 * 用户表
 */
@Getter
@Setter
@ToString
public class User extends BaseEntity {

    private Integer id;
    private String username;            //用户账号
    private String password;            //密码
    private String avatar;              //头像url
    private String email;               //邮箱
    private String nickname;            //用户昵称
    private Integer mytype;             //
    private Date createTime;            //创建时间
}