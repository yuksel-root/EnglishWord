package com.yuksi.worden.dataclass;

import android.provider.BaseColumns;


public final class TableContracts {

    public TableContracts() {
    }

    public static final class WordsTable implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_WORD_EN = "wordEn";
        public static final String COLUMN_WORD_TR = "wordTr";
        public static final String COLUMN_SENTENCE_EN = "sentenceEn";
        public static final String COLUMN_SENTENCE_TR = "sentenceTr";
        public static final String COLUMN_WORD_DATE = "wordDate";
        public static final String COLUMN_WORD_IMAGE = "wordImage";
    }

    public static final class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_QUESTION = "questions";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER = "answer";
        //public static final String COLUMN_QUIZ_IMAGE = "quizImage";

    }

    public static final class QuizInfoTable implements BaseColumns {
        public static final String TABLE_NAME = "quizInfo";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_QUIZ_DATE = "quizDate";
        public static final String COLUMN_QUIZ_NUMBER = "quizNumber";
        public static final String COLUMN_QUESTION_COUNT = "questionCount";
        public static final String COLUMN_TRUE_COUNT = "trueCount";
        public static final String COLUMN_FALSE_COUNT = "falseCount";
        public static final String COLUMN_QUIZ_SCORE = "quizScore";
    }
}
