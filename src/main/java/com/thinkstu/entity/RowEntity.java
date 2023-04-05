package com.thinkstu.entity;

import lombok.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/3, 20:05, 周一
 * 最后的行数据实体
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowEntity {
    /**
     * 特殊含义：
     * a == 1 ：表示为一整行，此时值为 b
     * c == 1/2/3 ：颜色代码
     */
    private String a;
    private String b;
    private String c;
    private String d;
}
