package bulletinboard;

import bulletinboard.service.BulletinBoardService;

public class Main {
    public static void main(String args[]) {
        new BulletinBoardService().start();
    }
}