package com.zt.mypassword.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/7 11:16
 * description: 正则表达式操作类
 */
public class RegularMatchUtils {


    public static final String YYYY_MM_DD = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
            "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
            "((0[48]|[2468][048]|[3579][26])00))-02-29)$";

    public static final String HH_MM = "^([0-1][0-9]|2[0-3]):([0-5][0-9])$";

    public static final String YYYY_MM_DD_HH_MM_SS = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|" +
            "((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|" +
            "((0[48]|[2468][048]|[3579][26])00))-02-29))" +
            "\\s([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";


    private static final int REGEX_OPTIONS = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;
    /**
     * 任意不包含HTML标签字符
     */
    public static final String MATCH_ALL_UN_HTML_REG = "[^<>]*?";

    public static final String MATCH_DIV_TEXT = "<div"+MATCH_ALL_UN_HTML_REG+">[^<]*</div>";

    public static final String MATCH_A_TEXT = "<a[^>]*>([^<]*)</a>";
    public static final String MATCH_A_HREF = "href[\\s]*?=[\\s]*?('|\")([^>|'|\"]*)('|\")";

    /**
     * Group提取 groupname=result
     */
    public static final String GROUP_RESULT_REG = "(?<result>[\\s\\S]*?)";

    public static final String MATCH_URL_2 = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";

    //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
    public static final String REG_SCRIPT = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
    public static final String REG_STYLE = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    //定义HTML标签的正则表达式
    public static final String REG_HTML = "<[^>]+>";
    public static final String REG_HTML_DIV_PREFIX = "<div[^>]*?>";
    public static final String REG_HTML_DIV_SUFFIX = "<\\/div>";
    public static final String REG_HTML_SPAN_PREFIX = "<span[^>]*?>";
    public static final String REG_HTML_SPAN_SUFFIX = "<\\/span>";
    public static final String REG_HTML_IMG = "<img[^>]*?>";
    //定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    public static final String REG_SPECIAL = "\\&[a-zA-Z]{1,10};";

    /**
     * 获取指定HTML标签的指定属性的值
     * @param source 要匹配的源文本
     * @param element 标签名称
     * @param attr 标签的属性名称
     * @return 属性值列表
     */
    public static List<String> getElementAttr(String source, String element, String attr) {
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
        return RegularMatchUtils.recursion(reg, source);
    }

    /**
     * Group数据格式构建  groupname=result
     *
     * @param reg 数据格式正则
     * @return String
     */
    public static String makeGroupResultReg(String reg) {
        return "(?<result>" + reg + ")";
    }

    public static String recursionGroupsFirst(String pattern, String content, String isNullReplace) throws Exception {
        List<String> patterns = Lists.newArrayList();
        patterns.add(pattern);
        List<String> contents = Lists.newArrayList();
        contents.add(content);
        List<String> temp = recursionGroups(patterns, contents);
        Optional<String> first = temp.stream().findFirst();
        return first.orElse(isNullReplace);
    }

    public static List<String> recursionGroups(List<String> patterns, List<String> contents) throws Exception {
        for (String sp : patterns) {
            if (!sp.contains("(?<result>")) {
                throw new Exception("表达" + sp + "式中缺少组“result”，例如所有字符" + GROUP_RESULT_REG);
            }
        }
        for (String pattern : patterns) {
            contents = recursionContent(pattern, contents, "result");
        }
        return contents;

    }

    public static String recursionFirst(String sPattern, String sContent, String isNullReplace) {
        List<String> sPatterns = Lists.newArrayList();
        sPatterns.add(sPattern);
        List<String> sContents = Lists.newArrayList();
        sContents.add(sContent);
        List<String> temp = recursion(sPatterns, sContents);
        Optional<String> first = temp.stream().findFirst();
        return first.orElse(isNullReplace);
    }

    /**
     * 正则匹配内容
     *
     * @param pattern 正则表达式列表
     * @param content 内容列表
     * @return List<String>
     */
    public static List<String> recursion(String pattern, String content) {
        List<String> patterns = Lists.newArrayList();
        patterns.add(pattern);
        List<String> contents = Lists.newArrayList();
        contents.add(content);
        return recursion(patterns, contents);
    }

    /**
     * 正则匹配，内容从正则的外层向内层层匹配
     *
     * @param patterns 正则表达式列表
     * @param contents 内容列表
     * @return List<String>
     */
    private static List<String> recursion(List<String> patterns, List<String> contents) {
        for (String pattern : patterns) {
            contents = recursionContent(pattern, contents, null);
        }
        return contents;
    }

    private static List<String> recursionContent(String pattern, List<String> contents, String group) {
        Pattern rx = Pattern.compile(pattern, REGEX_OPTIONS);
        return contents.stream()
                .map(content -> {
                    List<String> groupAll = Lists.newArrayList();
                    Matcher matches = rx.matcher(content);
                    while (matches.find()) {
                        if (StringUtils.isNotBlank(group)) {
                            groupAll.add(matches.group(group));
                        } else {
                            groupAll.add(matches.group());
                        }
                    }
                    return groupAll;
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static boolean recursionFind(String pattern, String content) {
        Pattern rx = Pattern.compile(pattern, REGEX_OPTIONS);
        Matcher matcher = rx.matcher(content);
        return matcher.find();
    }

    public static String recursionGroup(String pattern, String content,int group) {
        Pattern rx = Pattern.compile(pattern, REGEX_OPTIONS);
        Matcher matcher = rx.matcher(content);
        if (matcher.find()) {
            return matcher.group(group);
        }else{
            return StringUtils.EMPTY;
        }
    }
}
