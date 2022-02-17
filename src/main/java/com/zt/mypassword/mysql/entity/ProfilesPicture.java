package com.zt.mypassword.mysql.entity;

import com.zt.mypassword.enums.DeleteState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "t_profiles_picture")
public class ProfilesPicture {
    @Serial
    private static final long serialVersionUID =  1890258658838804132L;

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 路径
     */
    private String path;
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
