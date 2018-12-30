package be.witspirit.alexamenu;

import be.witspirit.alexamenu.menustore.DynamoDBMenuStore;
import be.witspirit.alexamenu.menustore.MenuStore;
import be.witspirit.alexamenu.menustore.SkillIds;
import be.witspirit.amazonlogin.AmazonProfileService;
import be.witspirit.amazonlogin.ProfileService;
import com.amazon.ask.AlexaSkill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

/**
 * Handler wrapper for the Alexa Menu Skill
 */
public class AlexaMenuHandler extends SkillStreamHandler {

    // Used by the normal invocation on AWS
    public AlexaMenuHandler() {
        this(new DynamoDBMenuStore(), new AmazonProfileService());
    }

    public AlexaMenuHandler(MenuStore menuStore, ProfileService profileService) {
        super(skillSetup(menuStore, profileService));
    }

    private static AlexaSkill skillSetup(MenuStore menuStore, ProfileService profileService) {
        return Skills.standard()
                .addRequestHandlers(
                        new LaunchHandler(),
                        new HelpHandler(),
                        new WhatsForDinnerHandler(profileService, menuStore),
                        new WrapUpHandler(),
                        new ContinueHandler(),
                        new FallbackHandler()
                )
                .withSkillId(SkillIds.MENU)
                // .withSkillId(SkillIds.CHEF)
                .build();
    }

}
