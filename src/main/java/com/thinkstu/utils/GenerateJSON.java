package com.thinkstu.utils;

import com.alibaba.fastjson2.*;
import com.thinkstu.comparator.*;
import com.thinkstu.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

/**
 * @author : ThinkStu
 * @since : 2023/4/4, 14:24, 周二
 **/
@Component
public class GenerateJSON {
    @Autowired
    PathRunnerAndUtils pathRunnerAndUtils;
    final DateTimeFormatter MMdd = DateTimeFormatter.ofPattern("MMdd");
    Pattern pattern = Pattern.compile("[^\\u4E00-\\u9FA5]+");   // 非中文匹配
    Pattern excludePattern10 = Pattern.compile(".*(WLA-(10[123]|30[1259]))|(WLC-112).*");  // 新校区需要排除的教室
    Pattern excludePattern2 = Pattern.compile("2-3-\\d.*|.*(102|108|303|407).*");  // 健翔桥校区需要排除的教室


    public void generate10(String data, LocalDateTime now, Integer time) {
        // 获得响应数据，rows 为获得的教室实体对象
        EmptyResultEntity                                     result = JSON.parseObject(data, EmptyResultEntity.class);
        List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> rows   = result.getDatas().getCxkxjs().getRows();
        LinkedList<String>                                    xxa    = new LinkedList<>();
        LinkedList<String>                                    xxb    = new LinkedList<>();
        LinkedList<String>                                    xxc    = new LinkedList<>();
        LinkedList<String>                                    xxd    = new LinkedList<>();
        LinkedList<String>                                    wla    = new LinkedList<>();
        LinkedList<String>                                    wlb    = new LinkedList<>();
        LinkedList<String>                                    wlc    = new LinkedList<>();
        LinkedHashMap<String, LinkedList>                           map    = new LinkedHashMap<>();
        map.put("文理楼A \uD83D\uDCD4 ", wla);
        map.put("文理楼B \uD83D\uDCD4 ", wlb);
        map.put("文理楼C \uD83D\uDCD4 ", wlc);
        map.put("信息楼A \uD83D\uDCBB ", xxa);
        map.put("信息楼B \uD83D\uDCBB ", xxb);
        map.put("信息楼C \uD83D\uDCBB ", xxc);
        map.put("信息楼D \uD83D\uDCBB ", xxd);
        // [^\u4E00-\u9FA5]+ 匹配非中文字符，新校区不需要含中文的教室
        // []表示字符集合^表示取反，\u4E00-\u9FA5表示Unicode中的中文字符范围，+表示匹配一次或多次。
        for (EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean row : rows) {
            String classroom = row.getJASMC();
            // 这里作了排除教室的设定
            if (pattern.matcher(classroom).matches() && !excludePattern10.matcher(classroom).matches()) {
                // 拆分教室名称，如 XXA-101，并添加至队列
                String[] split = classroom.split("-");
                String key = switch (split[0]) {
                    case "XXA" -> "信息楼A \uD83D\uDCBB ";
                    case "XXB" -> "信息楼B \uD83D\uDCBB ";
                    case "XXC" -> "信息楼C \uD83D\uDCBB ";
                    case "XXD" -> "信息楼D \uD83D\uDCBB ";
                    case "WLA" -> "文理楼A \uD83D\uDCD4 ";
                    case "WLB" -> "文理楼B \uD83D\uDCD4 ";
                    case "WLC" -> "文理楼C \uD83D\uDCD4 ";
                    default -> "skip";
                };
                if (key.equals("skip")) continue;
                map.get(key).addLast(split[1]);
            }
        }
        // 检测，如果全部为空！则标明教务网此次请求存在 error，这里就会抛出异常，就不会将数据写入文件
        checkAllEmpty(map);
        // 新增教室，教室未录入教务网，但是已经可以使用
//        map.get("文理楼A").addLast("408");
//        map.get("文理楼A").addLast("512");
//        map.get("文理楼C").addLast("109");
        // 手动删除一些无法使用的教室
        xxa.remove("301");
//        xxb.remove("101");
        // 文理 A/C 需要排序
        Collections.sort(wla, new IntegerComparator());
        Collections.sort(wlc, new IntegerComparator());
        // 如果当前时段没有空教室，则标记为“无”
        for (LinkedList linkedList : map.values()) {
            if (linkedList.size() == 0) {
                linkedList.addLast("无");
            }
        }
        System.out.println("昌平：" + now + " ：" + rows.size());

        // 输出至文件
        StringBuilder filename = new StringBuilder();
        filename.append(pathRunnerAndUtils.getPath()).append("/4/4").append(MMdd.format(now)).append(time).append(".json");
        try (PrintWriter printWriter = new PrintWriter(filename.toString())) {
            printWriter.print(JSON.toJSONString(map));
        } catch (Exception e) {
        }
    }

    public void generate1(String data, LocalDateTime now, Integer time) {
        // 获得响应数据，rows 为获得的教室实体对象
        EmptyResultEntity                                     result = JSON.parseObject(data, EmptyResultEntity.class);
        List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> rows   = result.getDatas().getCxkxjs().getRows();
        LinkedList<String>                                    one    = new LinkedList<>();
        LinkedList<String>                                    two    = new LinkedList<>();
        LinkedList<String>                                    fourth = new LinkedList<>();
        LinkedHashMap<String, LinkedList>                           map    = new LinkedHashMap<>();
        map.put("第一教学楼", one);
        map.put("第二教学楼", two);
        map.put("第四教学楼", fourth);

        // 小营校区不需要含中文的教室
        for (EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean row : rows) {
            String classroom = row.getJASMC();
            // 这里作了排除教室的设定
            if (pattern.matcher(classroom).matches()) {
                // 拆分教室名称，如 1-4-2003，并添加至队列，首部 1 为校区
                String[] split = classroom.split("-");
                String key = switch (split[1]) {
                    case "1" -> "第一教学楼";
                    case "2" -> "第二教学楼";
                    case "4" -> "第四教学楼";
                    default -> "skip";
                };
                if (key.equals("skip")) continue;
                map.get(key).addLast(split[2]);
            }
        }
        // 检测，如果全部为空！则标明教务网此次请求存在 error，这里就会抛出异常，就不会将数据写入文件
        checkAllEmpty(map);
        // 如果当前时段没有空教室，则标记为“无”
        for (LinkedList linkedList : map.values()) {
            if (linkedList.size() == 0) {
                linkedList.addLast("无");
            }
        }
        System.out.println("小营：" + now + " ：" + rows.size());
        // 输出至文件
        StringBuilder filename = new StringBuilder();
        filename.append(pathRunnerAndUtils.getPath()).append("/1/1").append(MMdd.format(now)).append(time).append(".json");

        try (PrintWriter printWriter = new PrintWriter(filename.toString())) {
            printWriter.print(JSON.toJSONString(map));
        } catch (Exception e) {
        }
    }

    private void checkAllEmpty(Map<String, LinkedList> map) {
        int check = 0;
        for (LinkedList linkedList : map.values()) {
            if (linkedList.size() != 0) check = 1;
        }
        if (check == 0) throw new RuntimeException("教务网数据为空！");
    }


    public void generate2(String data, LocalDateTime now, Integer time) {
        // 获得响应数据，rows 为获得的教室实体对象
        EmptyResultEntity                                     result = JSON.parseObject(data, EmptyResultEntity.class);
        List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> rows   = result.getDatas().getCxkxjs().getRows();
        LinkedList<String>                                    jt     = new LinkedList<>();
        LinkedList<String>                                    two    = new LinkedList<>();
        LinkedHashMap<String, LinkedList>                           map    = new LinkedHashMap<>();
        map.put("阶梯教室", jt);
        map.put("第二教学楼", two);

        for (EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean row : rows) {
            String classroom = row.getJASMC();
            // 这里作了排除教室的设定
            if (!excludePattern2.matcher(classroom).matches()) {
                // 拆分教室名称，如 2-2-425(原413)、2-2-421，并添加至队列
                String[] split = classroom.split("-");
                String key = switch (split[1]) {
                    case "2" -> "第二教学楼";
                    default -> "skip";
                };
                if (split[2].matches(".*阶.*")) {
                    key = "阶梯教室";
                }
                // 如果包含括号，则需要去除括号
                if (split[2].matches(".*\\(.*")) {
                    split[2] = split[2].substring(0, split[2].indexOf("("));
                }
                if (key.equals("skip")) continue;
                map.get(key).addLast(split[2]);
                // 对阶梯教室进行排序
                Collections.sort(jt, (s1, s2) -> {
                    int num1 = getNumber(s1);
                    int num2 = getNumber(s2);
                    return Integer.compare(num1, num2);
                });
            }
        }
        // 检测，如果全部为空！则标明教务网此次请求存在 error，这里就会抛出异常，就不会将数据写入文件
        checkAllEmpty(map);
        // 如果当前时段没有空教室，则标记为“无”
        for (LinkedList linkedList : map.values()) {
            if (linkedList.size() == 0) {
                linkedList.addLast("无");
            }
        }
        System.out.println("健翔桥：" + now + " ：" + rows.size());
        // 输出至文件
        StringBuilder filename = new StringBuilder();
        filename.append(pathRunnerAndUtils.getPath()).append("/2/2").append(MMdd.format(now)).append(time).append(".json");
        try (PrintWriter printWriter = new PrintWriter(filename.toString())) {
            printWriter.print(JSON.toJSONString(map));
        } catch (Exception e) {
        }
    }

    public void generate3(String data, LocalDateTime now, Integer time) {
        // 获得响应数据，rows 为获得的教室实体对象
        EmptyResultEntity                                     result = JSON.parseObject(data, EmptyResultEntity.class);
        List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> rows   = result.getDatas().getCxkxjs().getRows();
        LinkedList<String>                                    one    = new LinkedList<>();
        LinkedList<String>                                    two    = new LinkedList<>();
        LinkedList<String>                                    third  = new LinkedList<>();
        LinkedHashMap<String, LinkedList>                           map    = new LinkedHashMap<>();
        map.put("第一教学楼", one);
        map.put("第二教学楼", two);
        map.put("第三教学楼", third);

        for (EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean row : rows) {
            String classroom = row.getJASMC();
            // 拆分教室名称，如 3-3-101，并添加至队列
            String[] split = classroom.split("-");
            String key = switch (split[1]) {
                case "1" -> "第一教学楼";
                case "2" -> "第二教学楼";
                case "3" -> "第三教学楼";
                default -> "skip";
            };
            if (key.equals("skip")) continue;
            map.get(key).addLast(split[2]);
        }
        // 检测，如果全部为空！则标明教务网此次请求存在 error，这里就会抛出异常，就不会将数据写入文件
        checkAllEmpty(map);
        // 如果当前时段没有空教室，则标记为“无”
        for (LinkedList linkedList : map.values()) {
            if (linkedList.size() == 0) {
                linkedList.addLast("无");
            }
        }
        System.out.println("清河：" + now + " ：" + rows.size());
        // 输出至文件
        StringBuilder filename = new StringBuilder();
        filename.append(pathRunnerAndUtils.getPath()).append("/3/3").append(MMdd.format(now)).append(time).append(".json");
        try (PrintWriter printWriter = new PrintWriter(filename.toString())) {
            printWriter.print(JSON.toJSONString(map));
        } catch (Exception e) {
        }
    }

    // 健翔桥排序专用
    private static int getNumber(String str) {
        switch (str) {
            case "一阶梯":
                return 1;
            case "二阶梯":
                return 2;
            case "三阶梯":
                return 3;
            case "四阶梯":
                return 4;
            case "五阶梯":
                return 5;
            case "六阶梯":
                return 6;
            case "七阶梯":
                return 7;
            case "八阶梯":
                return 8;
            default:
                return 0;
        }
    }
}
