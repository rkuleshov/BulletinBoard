package bulletinBoard.service;

import bulletinBoard.dao.AdvertDAO;
import bulletinBoard.dao.RubricDAO;
import bulletinBoard.model.Advert;
import bulletinBoard.model.Rubric;
import bulletinBoard.model.User;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdvertService {

    AdvertDAO advertDAO = new AdvertDAO();
    InputValidation inputValidation = new InputValidation();

    private List<Advert> adverts;

    public List<Advert> getAdverts() {
        return adverts;
    }

    public AdvertService() {

        selectFileType();
    }

    // selects a file from which will read data
    private void selectFileType() {
        boolean correctNumberFlag;
        int number = 0;
        do {
            Scanner in = new Scanner(System.in);
            correctNumberFlag = true;
            System.out.println("Enter 1 to read data from xml file");
            System.out.println("Enter 2 to read data from json file");
            try {
                number = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You must enter a digit");
                correctNumberFlag = false;
                in.next();
            }
        }

        while (!correctNumberFlag || !inputValidation.isCorrectInput(number, 1, 2));

        switch (number) {

            case 1:
                adverts = advertDAO.getAdvertsFromXML();
                break;
            case 2:
                adverts = advertDAO.getAllAdvertsFromJSON();
                break;
        }
    }

    public void displayAdvertsByRubricId(int numberOfRubric) {

        System.out.println("Adverts in rubric");
        System.out.println("");
        for (Advert advert : adverts) {
            if (advert.getRubricId() == numberOfRubric) {
                System.out.println(advert);
                System.out.println("");
            }
        }
    }

    public List<Advert> getAdvertsByUserId(int userId) {
        List<Advert> advertsByUser = new ArrayList<Advert>();
        for (Advert advert : adverts) {
            if (advert.getUserId() == userId) {
                advertsByUser.add(advert);
            }
        }
        return advertsByUser;

    }

    public void displayAdverts(List<Advert> adverts) {
        for (Advert advert : adverts) {
            System.out.println(advert);
            System.out.println("");
        }
    }

    public void deleteAdvert(Advert currentAdvert, User user, List<User> users) {

        for (ListIterator<Advert> i = adverts.listIterator(); i.hasNext(); ) {
            Advert advert = i.next();
            if (advert.equals(currentAdvert)) {
                i.remove();
            }
        }

//check whether there are any more posts by this author
// if not, remove it from the collection of users
        if (!isUserHasAdverts(user)) {
            for (ListIterator<User> i = users.listIterator(); i.hasNext(); ) {
                User u = i.next();
                if (u.equals(user)) {
                    i.remove();
                }
            }
        }

    }

    private boolean isUserHasAdverts(User user) {
        for (Advert advert : adverts) {
            if (advert.getUserId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    public void editAdvert(Advert currentAdvert, int userId) {
        Advert advert = new Advert();

        advert.setUserId(userId);
        advert.setId(currentAdvert.getId());
        advert.setRubricId(selectRubric());
        advert.setTitle(getTitle());
        advert.setText(getText());
        advert.setPublicationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        adverts.add(advert);
        System.out.println("Advert successfully edited");
    }

    public void createAdvert(User user, List<User> users) {
        Advert advert = new Advert();

        advert.setUserId(user.getId());
        advert.setId(adverts.size() + 1);
        advert.setRubricId(selectRubric());
        advert.setTitle(getTitle());
        advert.setText(getText());
        advert.setPublicationDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        if(!users.contains(user)){
            users.add(user);
        }
        adverts.add(advert);
        System.out.println("Advert successfully created");

    }

    private String getText() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the text");

        String text = "";
        do {
            System.out.println("Between 20 and 400 characters");
            text = in.nextLine();
        }
        while (text.length() < 20 || text.length() > 400);
        return text;
    }

    private String getTitle() {
        Scanner in = new Scanner(System.in);
        String title = "";
        System.out.println("Enter a new title");
        do {
            System.out.println("between 10 and 30 characters");
            title = in.nextLine();
        }
        while (title.length() < 10 || title.length() > 30);
        return title;
    }

    private int selectRubric() {
        RubricDAO rubricDAO = new RubricDAO();
        List<Rubric> rubrics = rubricDAO.getRubrics();
        int rubricId = 0;
        boolean correctNumberFlag = true;
        Scanner in = new Scanner(System.in);
        do {
            correctNumberFlag = true;
            System.out.println("Select one of the rubrics by entering its number: ");
            for (Rubric rubric : rubrics) {
                System.out.println(rubric);
            }
            try {

                rubricId = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You must enter a digit");
                correctNumberFlag = false;
                in.next();
            }

        }
        while (!correctNumberFlag || !inputValidation.isCorrectInput(rubricId, 1, rubrics.size()));
        return rubricId;
    }
}
