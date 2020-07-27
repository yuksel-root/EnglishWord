package com.yuksi.worden.dataclass;

public class Words {
    private int wordId;
    private String wordEn;
    private String wordTr;
    private String sentenceEn;
    private String sentenceTr;
    private String wordDate;
    private int wordCount;
    private byte[] byteArray;

    public Words() {
    }

    public Words(int wordCount) {
        this.wordCount = wordCount;
    }

    public Words(String wordEn, String wordTr, String sentenceEn, String sentenceTr, byte[] byteArray) {
        this.wordEn = wordEn;
        this.wordTr = wordTr;
        this.sentenceEn = sentenceEn;
        this.sentenceTr = sentenceTr;
        this.byteArray = byteArray;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getWordDate() {
        return wordDate;
    }

    public void setWordDate(String wordDate) {
        this.wordDate = wordDate;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }


    public String getWordEn() {
        return wordEn;
    }

    public String getWordTr() {
        return wordTr;
    }

    public String getSentenceEn() {
        return sentenceEn;
    }

    public String getSentenceTr() {
        return sentenceTr;
    }

    public void setWordEn(String wordEn) {
        this.wordEn = wordEn;
    }

    public void setWordTr(String wordTr) {
        this.wordTr = wordTr;
    }

    public void setSentenceEn(String sentenceEn) {
        this.sentenceEn = sentenceEn;
    }

    public void setSentenceTr(String sentenceTr) {
        this.sentenceTr = sentenceTr;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
