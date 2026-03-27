package com.doow.rubbish.generator;


public class CodeBuilder {
    private StringBuilder sb;

    public CodeBuilder() {
        this.sb = new StringBuilder();
    }

    public CodeBuilder append(String str) {
        sb.append(str);
        return this;
    }

    public CodeBuilder appendLine(String str) {
        sb.append(str).append("\n");
        return this;
    }

    public CodeBuilder appendComment(String comment) {
        sb.append("/**\n");
        sb.append(" * ").append(comment).append("\n");
        sb.append(" */\n");
        return this;
    }

    public CodeBuilder appendEmptyLine() {
        sb.append("\n");
        return this;
    }

    public String toString() {
        return sb.toString();
    }
}
