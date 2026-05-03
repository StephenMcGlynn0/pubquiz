package ie.stephen.pubquiz.model;

public class CategoryInfo {
    private String name;
    private int questionCount;

    public CategoryInfo(String name, int questionCount) {
        this.name = name;
        this.questionCount = questionCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}