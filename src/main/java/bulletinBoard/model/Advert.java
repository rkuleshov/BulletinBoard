package bulletinBoard.model;

public class Advert {

    private int userId;
    private String publicationDate;
    private int rubricId;
    private int id;
    private String title;
    private String text;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getRubricId() {
        return rubricId;
    }

    public void setRubricId(int rubricId) {
        this.rubricId = rubricId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Advert advert = (Advert) o;

        if (id != advert.id) return false;
        if (rubricId != advert.rubricId) return false;
        if (userId != advert.userId) return false;
        if (publicationDate != null ? !publicationDate.equals(advert.publicationDate) : advert.publicationDate != null)
            return false;
        if (text != null ? !text.equals(advert.text) : advert.text != null) return false;
        if (title != null ? !title.equals(advert.title) : advert.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        result = 31 * result + rubricId;
        result = 31 * result + id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "id: " +  id + " | Publication date: " + publicationDate + " | Title: " + title +  " | Text: " + text;
    }
}
