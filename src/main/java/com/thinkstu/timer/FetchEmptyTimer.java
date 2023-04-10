package com.thinkstu.timer;

import com.thinkstu.entity.*;
import com.thinkstu.utils.*;
import jakarta.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/4, 08:18, 周二
 **/
@Slf4j
@Component
public class FetchEmptyTimer {
    @Autowired
    ExecutorService executor;
    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    GenerateJSON generateJSON;
    @Autowired
    RequestUtils requestUtils;
    // 校区：1小营，2健翔桥，3清河，10昌平
    Integer campus = 1;
    final DateTimeFormatter YYYY_MM_dd = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    HashMap<Integer, int[]> hashMap;


    @Scheduled(initialDelay = 1_000, fixedRate = 10800_000)
    void autoFetch() throws Exception {
        String        cookie = cookieUtils.get();       // 获取 cookie，如果不可用则会自动更新一次
        LocalDateTime now    = LocalDateTime.now();     // 获取今天的日期，格式范例：2023-04-04
//        LocalDateTime now     = LocalDateTime.of(2023, 4, 30, 12, 0);
        // 测试，只查询 1 天
        for (int i = 1; i < 5; i++) {
            fetchSomeday(cookie, now.plus(i, ChronoUnit.DAYS));
            Thread.sleep(3000);
        }
    }

    private void fetchSomeday(String cookie, LocalDateTime now) throws Exception {
        ParamEntity param1  = new ParamEntity(YYYY_MM_dd.format(now), 1);
        ParamEntity param2  = new ParamEntity(YYYY_MM_dd.format(now), 2);
        ParamEntity param3  = new ParamEntity(YYYY_MM_dd.format(now), 3);
        ParamEntity param10 = new ParamEntity(YYYY_MM_dd.format(now), 10);
        // for 循环，从 1~8，对应我们定义的时段
        for (int i = 1; i < 9; i++) {
            int[] times = hashMap.get(i);
            executor.execute(() -> {
                        generateJSON.generate1(requestUtils.post(cookie, param1.setTime(times[1], times[2])), now, times[0]);
                        generateJSON.generate2(requestUtils.post(cookie, param2.setTime(times[1], times[2])), now, times[0]);
                        generateJSON.generate3(requestUtils.post(cookie, param3.setTime(times[1], times[2])), now, times[0]);
                        generateJSON.generate10(requestUtils.post(cookie, param10.setTime(times[1], times[2])), now, times[0]);
                        log.info("===》{} ，第 {} 次执行完毕！", now.format(YYYY_MM_dd), times[0]);
                    }
            );
        }
    }

    // 初始化一些数据，这里为：时段划分、开始节次、结束节次
    @PostConstruct
    void initial() {
        hashMap = new HashMap<>();
        hashMap.put(1, new int[]{1, 1, 12});
        hashMap.put(2, new int[]{2, 1, 5});
        hashMap.put(3, new int[]{3, 1, 2});
        hashMap.put(4, new int[]{4, 3, 5});
        hashMap.put(5, new int[]{5, 6, 9});
        hashMap.put(6, new int[]{6, 6, 7});
        hashMap.put(7, new int[]{7, 8, 9});
        hashMap.put(8, new int[]{8, 10, 12});
    }
}
