package com.liuxu.mq.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 * @date: 2025-11-09
 * @author: liuxu
 */
public abstract class ExceptionUtil {


    /**
     * 获取指定错误的所有栈信息字符串
     *
     * @param t 错误
     * @return
     */
    public static String printStackTraceToString(Throwable t) {
        try (StringWriter sw = new StringWriter();
             PrintWriter printWriter = new PrintWriter(sw, true)) {

            t.printStackTrace(printWriter); // 第二个参数 true，它启用了自动刷新 模式
            // try-with-resources 会自动调用 pw.close()，而 pw.close() 会调用 sw.close()
            // 尽管对于 StringWriter 来说，close() 是一个空操作，但这是一个好习惯
            return sw.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException("无法将堆栈跟踪转换为字符串", e);
        }
    }

}
