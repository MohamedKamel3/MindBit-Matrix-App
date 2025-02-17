package matrixx;

import static matrixx.SoundPlayer.playSound;
import static matrixx.pages.Info_Page.Info.ShowInfoPage;
import matrixx.pages.home_page.HomePage;
import static matrixx.pages.home_page.HomePage.HideHomePage;

public class main {

    public static void main(String[] args) {
        HomePage page = new HomePage();
        HideHomePage();
        ShowInfoPage();
        playSound("matrix/src/main/java/sound/startup.wav");

    }
}
