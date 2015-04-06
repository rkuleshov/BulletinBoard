package bulletinboard.service;

import bulletinboard.dao.RubricDAO;
import bulletinboard.model.Rubric;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RubricService {
    private RubricDAO rubricDAO = new RubricDAO();
    private List<Rubric> rubrics = rubricDAO.getRubrics();
    private InputValidation inputValidation = new InputValidation();

    public void displayRubrics(AdvertService advertService) {
        for (Rubric rubric : rubrics) {
            System.out.println(rubric);
        }

        advertService.displayAdvertsByRubricId(getNumberOfRubric());
    }

    public int getNumberOfRubric() {
        int numberOfRubric = 0;
        boolean correctNumberFlag;
        do {
            correctNumberFlag = true;
            System.out.println("Enter the number of required rubric");
            try {
                Scanner in = new Scanner(System.in);
                numberOfRubric = in.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("You must enter a digit");
                correctNumberFlag = false;
            }
        }
        while (!correctNumberFlag || !inputValidation.isCorrectInput(numberOfRubric, 1, rubrics.size()));
        return numberOfRubric;
    }
}
