package com.yuksi.worden.dataclass;

public class Quiz {
    private int quizId;
    private String quizDate;
    private int quizNumber;
    private int questionCount;
    private int quizScore;
    private int trueCount;
    private int falseCount;

    public Quiz() {

    }

    public Quiz(String quizDate, int quizNumber, int questionCount, int trueCount, int falseCount, int quizScore) {
        this.quizDate = quizDate;
        this.quizNumber = quizNumber;
        this.questionCount = questionCount;
        this.trueCount = trueCount;
        this.falseCount = falseCount;
        this.quizScore = quizScore;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(int quizNumber) {
        this.quizNumber = quizNumber;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(int quizScore) {
        this.quizScore = quizScore;
    }

    public int getTrueCount() {
        return trueCount;
    }

    public void setTrueCount(int trueCount) {
        this.trueCount = trueCount;
    }

    public int getFalseCount() {
        return falseCount;
    }

    public void setFalseCount(int falseCount) {
        this.falseCount = falseCount;
    }
}
