package waste.factory.resource;

/**
 * Created by miha on 22.04.17
 */
public interface WasteFactoryStrings {

    String GAME_NAME = "Waste Factory";

    String INVITATION_SPLASH = "Tap anywhere to begin.";

    String WASTE_PAPER = "paper";
    String WASTE_CARTON = "carton";
    String WASTE_ASEPTIC = "Tetra Pak";
    String WASTE_SAD = "assorted waste";

    String WRONG_WASTE_FORMAT = "%2$s is not %1$s."; // expected, actual -> {actual} is not {expected}.
    String SECONDS_LEFT_FORMAT = "%d seconds left"; // todo: plurals

    String LEVEL_FAILED = "You lose!";
    String LEVEL_RETRY = "Tap screen to try again.";

    String LEVEL_SUCCEED = "You win!";
    String LEVEL_ONE_MORE_TIME = "Tap screen to play one more time.";

}
