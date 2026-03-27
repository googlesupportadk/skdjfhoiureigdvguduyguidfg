package com.doow.rubbish.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

    private static Logger instance;

    private FileWriter logWriter;
    private boolean consoleOutput;
    private boolean fileOutput;
    private String logFilePath;
    private SimpleDateFormat dateFormat;

    private Logger() {
        this.consoleOutput = true;
        this.fileOutput = true;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }


    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }


    public void initLogFile(String projectRoot) {
        if (!fileOutput) {
            return;
        }

        try {
            File logDir = new File(projectRoot, "logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            logFilePath = logDir.getAbsolutePath() + "/generator_" + timestamp + ".log";
            logWriter = new FileWriter(logFilePath);

            info("日志文件已创建: " + logFilePath);
        } catch (IOException e) {
            System.err.println("无法创建日志文件: " + e.getMessage());
            fileOutput = false;
        }
    }

    /**
     * 设置控制台输出
     */
    public void setConsoleOutput(boolean enabled) {
        this.consoleOutput = enabled;
    }

    /**
     * 设置文件输出
     */
    public void setFileOutput(boolean enabled) {
        this.fileOutput = enabled;
    }

    /**
     * 记录信息
     */
    public void info(String message) {
        log("INFO", message);
    }

    /**
     * 记录警告
     */
    public void warning(String message) {
        log("WARN", message);
    }

    /**
     * 记录错误
     */
    public void error(String message) {
        log("ERROR", message);
    }

    /**
     * 记录错误（带异常）
     */
    public void error(String message, Throwable cause) {
        log("ERROR", message);
        if (cause != null) {
            String stackTrace = getStackTrace(cause);
            log("ERROR", stackTrace);
        }
    }

    /**
     * 记录调试信息
     */
    public void debug(String message) {
        log("DEBUG", message);
    }

    /**
     * 记录阶段开始
     */
    public void phaseStart(String phaseName) {
        String message = "========================================\n" +
                        "阶段开始: " + phaseName + "\n" +
                        "========================================";
        log("PHASE", message);
    }

    /**
     * 记录阶段完成
     */
    public void phaseComplete(String phaseName) {
        String message = "========================================\n" +
                        "阶段完成: " + phaseName + "\n" +
                        "========================================";
        log("PHASE", message);
    }

    /**
     * 记录进度
     */
    public void progress(int current, int total, String message) {
        String formattedMessage = String.format("[%d/%d] %s", current, total, message);
        log("PROGRESS", formattedMessage);
    }

    /**
     * 内部日志方法
     */
    private void log(String level, String message) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);

        if (consoleOutput) {
            System.out.println(logEntry);
        }

        if (fileOutput && logWriter != null) {
            try {
                logWriter.write(logEntry + "\n");
                logWriter.flush();
            } catch (IOException e) {
                System.err.println("写入日志失败: " + e.getMessage());
            }
        }
    }

    /**
     * 获取堆栈跟踪
     */
    private String getStackTrace(Throwable cause) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        cause.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 关闭日志文件
     */
    public void close() {
        if (logWriter != null) {
            try {
                logWriter.close();
                info("日志文件已关闭: " + logFilePath);
            } catch (IOException e) {
                System.err.println("关闭日志文件失败: " + e.getMessage());
            }
            logWriter = null;
        }
    }

    /**
     * 获取日志文件路径
     */
    public String getLogFilePath() {
        return logFilePath;
    }
}
