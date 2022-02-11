package com.zt.mypassword.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/14 14:02
 * description: 开启状态
 */
@Getter
@AllArgsConstructor
public enum EnabledState implements Serializable {
    OFF, ON
}
