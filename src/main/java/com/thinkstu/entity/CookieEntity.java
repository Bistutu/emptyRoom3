package com.thinkstu.entity;

import lombok.*;

/**
 * @author : ThinkStu
 * @since : 2023/3/31, 16:34, 周五
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CookieEntity {
    private String rootCookie;  // 根 Cookie
    private String emptyCookie; // 空教室查询页cookie
}
