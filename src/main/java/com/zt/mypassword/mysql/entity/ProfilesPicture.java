package com.zt.mypassword.mysql.entity;

import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.EnabledState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "t_profiles_picture")
public class ProfilesPicture implements Serializable {
    @Serial
    private static final long serialVersionUID = 1890258658838804132L;

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 路径
     */
    private String path;
    /**
     * 路径
     */
    @Enumerated(value = EnumType.STRING)
    private EnabledState enabledState;
    /**
     * 删除状态
     */
    @Enumerated(value = EnumType.STRING)
    private DeleteState deleteState;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
}
