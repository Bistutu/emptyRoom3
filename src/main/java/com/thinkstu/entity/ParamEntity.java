package com.thinkstu.entity;

import lombok.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/4, 10:27, 周二
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamEntity {
    String date;
    Integer XXXQDM;
    Integer KSJC;
    Integer JSJC;

    public ParamEntity setTime(Integer KSJC, Integer JSJC) {
        this.KSJC = KSJC;
        this.JSJC = JSJC;
        return this;
    }

    public ParamEntity(String date, Integer XXXQDM) {
        this.date = date;
        this.XXXQDM = XXXQDM;
    }
}
