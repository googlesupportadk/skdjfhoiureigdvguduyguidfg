package com.doow.rubbish.generator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class ErrorHandler {

    private static ErrorHandler instance;
    private List<ErrorInfo> errors;
    private boolean strictMode;

    private ErrorHandler() {
        this.errors = new ArrayList<>();
        this.strictMode = false;
    }


    public static synchronized ErrorHandler getInstance() {
        if (instance == null) {
            instance = new ErrorHandler();
        }
        return instance;
    }


    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }


    public void handleError(String phase, String message, Throwable cause) {
        ErrorInfo errorInfo = new ErrorInfo(phase, message, cause);
        errors.add(errorInfo);

        System.err.println("\n========================================");
        System.err.println("错误发生");
        System.err.println("========================================");
        System.err.println("阶段: " + phase);
        System.err.println("消息: " + message);

        if (cause != null) {
            System.err.println("异常: " + cause.getClass().getName());
            System.err.println("详情: " + cause.getMessage());
            System.err.println("----------------------------------------");
            System.err.println("堆栈跟踪:");
            cause.printStackTrace(System.err);
        }

        System.err.println("========================================");

        if (strictMode) {
            throw new GenerationException(message, cause);
        }
    }

    /**
     * 处理警告
     */
    public void handleWarning(String phase, String message) {
        System.out.println("\n⚠ 警告 [" + phase + "]: " + message);
    }

    /**
     * 获取错误列表
     */
    public List<ErrorInfo> getErrors() {
        return new ArrayList<>(errors);
    }

    /**
     * 获取错误数量
     */
    public int getErrorCount() {
        return errors.size();
    }

    /**
     * 是否有错误
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * 清除所有错误
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * 打印错误摘要
     */
    public void printErrorSummary() {
        if (errors.isEmpty()) {
            System.out.println("\n✓ 没有错误");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("错误摘要");
        System.out.println("========================================");
        System.out.println("总错误数: " + errors.size());
        System.out.println("----------------------------------------");

        for (int i = 0; i < errors.size(); i++) {
            ErrorInfo error = errors.get(i);
            System.out.println((i + 1) + ". " + error.phase);
            System.out.println("   " + error.message);
            if (error.cause != null) {
                System.out.println("   " + error.cause.getClass().getSimpleName() + 
                                 ": " + error.cause.getMessage());
            }
            System.out.println();
        }

        System.out.println("========================================");
    }

    /**
     * 错误信息类
     */
    public static class ErrorInfo {
        public final String phase;
        public final String message;
        public final Throwable cause;

        public ErrorInfo(String phase, String message, Throwable cause) {
            this.phase = phase;
            this.message = message;
            this.cause = cause;
        }
    }

    /**
     * 生成异常类
     */
    public static class GenerationException extends RuntimeException {
        public GenerationException(String message) {
            super(message);
        }

        public GenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
