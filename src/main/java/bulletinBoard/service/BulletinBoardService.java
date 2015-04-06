package bulletinBoard.service;

import bulletinBoard.dao.AdvertDAO;
import bulletinBoard.dao.UserDAO;
import bulletinBoard.model.Advert;
import bulletinBoard.model.User;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BulletinBoardService {

    AdvertService advertService = new AdvertService();
    RubricService rubricService = new RubricService();
    InputValidation inputValidation = new InputValidation();
    AdvertDAO advertDAO = new AdvertDAO();
    UserDAO userDAO = new UserDAO();

    User user = new User();

    private List<User> users = userDAO.getAllUsers();
    private List<Advert> adverts = advertService.getAdverts();

    public List<Advert> getAdverts() {
        return adverts;
    }

    public List<User> getUsers() {
        return users;
    }

    //get the number of the option that should be executed
    public int getOptionNumberOfMainMenu() {
        int number = 0;
        boolean correctNumberFlag = true;
        do {
            correctNumberFlag = true;
            System.out.println("Enter 1 to show your adverts");
            System.out.println("Enter 2 to look adverts by rubric");
            System.out.println("Enter 3 to look adverts by user");
            System.out.println("Enter 4 to create a new advert");
            System.out.println("Enter 5 to exit program");

            try {
                Scanner in = new Scanner(System.in);
                number = in.nextInt();
            } catch (InputMismatchException e) {
                correctNumberFlag = false;
                System.out.println("You must enter a digit");

            }
        }
        while (!correctNumberFlag || !inputValidation.isCorrectInput(number, 1, 5));

        return number;
    }

    public void displayUserAdverts() {

        List<Advert> advertsByUser = new ArrayList<Advert>();
        if (users.contains(user)) {
            System.out.println("Your adverts:");
            advertService.displayAdverts(advertService.getAdvertsByUserId(user.getId()));
            advertsByUser = advertService.getAdvertsByUserId(user.getId());
        } else {
            System.out.println("You did not create any adverts");
            System.out.println(" ");
            goToMainMenu();
        }

        displayCurrentAdvert(advertsByUser);

    }

//execution jf option selected by the user
    private void performAdvertOption(Advert currentAdvert, int number) {
        switch (number) {
            case 1:
                advertService.deleteAdvert(currentAdvert, user, users);
                System.out.println("Advert is removed");
                goToMainMenu();
                break;
            case 2:
                advertService.editAdvert(currentAdvert, user.getId());
                advertService.deleteAdvert(currentAdvert, user, users);
                goToMainMenu();
                break;
            case 3:
                goToMainMenu();
                break;
            case 4:
                programExit();
                break;
        }
    }

    //get the number of the option that should be executed
    private int getNumberOfAdvertOption() {
        int number = 0;
        boolean correctNumberFlag;
        Scanner in = new Scanner(System.in);
        do {
            correctNumberFlag = true;
            System.out.println("Enter 1 to remove advert");
            System.out.println("Enter 2 to edit advert");
            System.out.println("Enter 3 to go to the main menu");
            System.out.println("Enter 4 to exit the program");

            try {
                number = in.nextInt();
            } catch (InputMismatchException e) {
                correctNumberFlag = false;
                System.out.println("You must enter a digit");
                in.next();
            }
        }
        while (!inputValidation.isCorrectInput(number, 1, 4) || !correctNumberFlag);

        return number;

    }

    public void displayCurrentAdvert(List<Advert> advertsByUser) {
        Advert currentAdvert = null;
        boolean correctNumberFlag;
        Scanner in = new Scanner(System.in);
        do {
            System.out.println("Enter the advert id to go to the options");
            int advertId = 0;
            correctNumberFlag = true;
            try {


                advertId = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You must enter a digit");
                correctNumberFlag = false;
                in.next();
            }

            for (Advert advert : advertsByUser) {
                if (advert.getId() == advertId) {
                    currentAdvert = advert;
                }
            }
        }
        while (!correctNumberFlag || currentAdvert == null);
        System.out.println(currentAdvert);

        performAdvertOption(currentAdvert, getNumberOfAdvertOption());

    }

    public void displayByUser() {
        int authorNumber = 0;
        boolean correctNumberFlag = true;
        do {
            correctNumberFlag = true;
            for (User user : users) {
                System.out.println(user);
            }
            System.out.println("Enter the number of the required author");
            try {
                Scanner in = new Scanner(System.in);
                authorNumber = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You must enter a digit");
                correctNumberFlag = false;
            }


        }
        while (!correctNumberFlag || !inputValidation.isCorrectInput(authorNumber, 1, users.size()));


        for (User user : users) {
            if (authorNumber == user.getId()) {
                System.out.println("All adverts by author " + user.getUserName() + ":");
                advertService.displayAdverts(advertService.getAdvertsByUserId(user.getId()));
            }
        }

    }

    //execution of option chosen by the user in the main menu
    public void performOptionOfMainMenu() {
        switch (getOptionNumberOfMainMenu()) {
            case 1:
                displayUserAdverts();
                goToMainMenu();
                break;
            case 2:
                rubricService.displayRubrics(advertService);
                goToMainMenu();
                break;
            case 3:
                displayByUser();
                goToMainMenu();
                break;
            case 4:
                advertService.createAdvert(user, users);
                displayUserAdverts();
                break;
            case 5:
                programExit();
                break;
        }

    }


    public void start() {
        login();
        goToMainMenu();
    }

    private void login() {
        System.out.println("Enter your name");
        String userName = "";
        do {
            System.out.println("Requires from 4 to 20 characters in Roman letters or numbers (the first character must be a letter)");
            Scanner in = new Scanner(System.in);
            userName = in.nextLine();

        }
        while (userName.equals("") || !inputValidation.isFirstCharacterIsLetter(userName) || inputValidation.isContainsNonLatinCharacters(userName) || userName.length() < 4 || userName.length() > 20);

        user.setUserName(userName);

        if (!users.contains(user)) {
            user.setId(users.size() + 1);
        } else {
            for (User u : users) {
                if (user.equals(u)) {
                    user = u;
                }
            }

        }
    }

    public void goToMainMenu() {
        performOptionOfMainMenu();
    }

//before closing the program, stores all users and all adverts in XML and JSON files
    public void programExit() {
        advertDAO.saveAllAdvertsToXML(adverts);
        userDAO.saveAllUsers(users);
        advertDAO.saveAllAdvertsToJSON(adverts);
        System.exit(0);

    }
}
