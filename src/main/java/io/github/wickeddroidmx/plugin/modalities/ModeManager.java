package io.github.wickeddroidmx.plugin.modalities;

import io.github.wickeddroidmx.plugin.modalities.modes.*;
import io.github.wickeddroidmx.plugin.modalities.scenarios.*;
import io.github.wickeddroidmx.plugin.modalities.teams.ChosenMode;
import io.github.wickeddroidmx.plugin.modalities.teams.FriendlyFireMode;
import io.github.wickeddroidmx.plugin.modalities.teams.LoveAtFirstSightMode;
import io.github.wickeddroidmx.plugin.modalities.uhc.UhcRunMode;
import io.github.wickeddroidmx.plugin.modalities.uhc.UhcSkyHighMode;
import me.yushust.inject.InjectAll;

import javax.inject.Singleton;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@InjectAll
@Singleton
public class ModeManager {

    //Modalities
    private TanksMode tanksMode;
    private NoNetherMode noNetherMode;
    private NoAchievementsMode noAchievementsMode;
    private NoNameTagMode noNametagMode;
    // private HealthDarkStartMode healthDarkStartMode;
    private AdvancedTacticsMode advancedTacticsMode;
    private KingMode kingMode;
    private NoVillagesMode noVillagesMode;
    private UltraAggressiveMode ultraAggressiveMode;
    private BackpackMode backpackMode;
    private JumperMode jumperMode;
    private DeathListMode deathListMode;
    private UhcSpainMode uhcSpainMode;

    private UltraDropsRandomMode ultraDropsRandomMode;

    private DropsRandomMode dropsRandomMode;

    private SecondLifeMode secondLifeMode;

    private MolesMode molesMode;

    private EncyclopediaMode encyclopediaMode;

    private MoreOresMode moreOresMode;

    private ColdWeaponsMode coldWeaponsMode;

    private GoldenHeadMode goldenHeadMode;

    private DamageCycleMode damageCycleMode;

    private SilentNightMode silentNightMode;

    // Team
    private LoveAtFirstSightMode loveAtFirstSightMode;

    private ChosenMode chosenMode;

    private FriendlyFireMode friendlyFireMode;

    //Scenarios
    private GunsNRosesScenario gunsNRosesScenario;
    private GraveRobbersScenario graveRobbersScenario;
    private ChickenScenario chickenScenario;
    private ChickenFightScenario chickenFightScenario;
    private NoFallScenario noFallScenario;
    private SuperHeroesMode superHeroesMode;
    private HasteyBoysScenario hasteyBoysScenario;
    private TimberScenario timberScenario;
    private CutCleanScenario cutCleanScenario;
    private FireLessScenario fireLessScenario;
    private SiphonScenario siphonScenario;
    private NoMobsScenario noMobsScenario;
    private TeamInventoryScenario teamInventoryScenario;
    private RegretFulWolfsScenario regretFulWolfsScenario;
    private DoggosMode doggosMode;
    private NuggetsScenario nuggetsScenario;
    private StarterBookScenario starterBookScenario;
    private AnvilDiscountsScenario anvilDiscountsScenario;

    private NoBurnScenario noBurnScenario;
    private TradeParanoia tradeParanoia;
    private CobwebLess cobwebLess;

    private ShieldLessScenario shieldLessScenario;

    //UHC
    private UhcRunMode uhcRunMode;

    private UhcSkyHighMode uhcSkyHighMode;

    private final HashMap<String, Modality> modeMap = new HashMap<>();

    public void     registerModes() {
        registerMode(
                tanksMode,
                kingMode,
                noVillagesMode,
                noNetherMode,
                uhcRunMode,
                gunsNRosesScenario,
                timberScenario,
                graveRobbersScenario,
                hasteyBoysScenario,
                cutCleanScenario,
                chickenFightScenario,
                chickenScenario,
                loveAtFirstSightMode,
                fireLessScenario,
                noFallScenario,
                tradeParanoia,
                siphonScenario,
                teamInventoryScenario,
              //healthDarkStartMode,
                superHeroesMode,
                ultraAggressiveMode,
                jumperMode,
                backpackMode,
                noNametagMode,
                noAchievementsMode,
                noMobsScenario,
                advancedTacticsMode,
                regretFulWolfsScenario,
                nuggetsScenario,
                doggosMode,
                deathListMode,
                uhcSpainMode,
                cobwebLess,
                ultraDropsRandomMode,
                dropsRandomMode,
                starterBookScenario,
                secondLifeMode,
                molesMode,
                chosenMode,
                uhcSkyHighMode,
                anvilDiscountsScenario,
                noBurnScenario,
                encyclopediaMode,
                coldWeaponsMode,
                shieldLessScenario,
                friendlyFireMode,
                goldenHeadMode,
                damageCycleMode,
                silentNightMode
        );
    }


    private void registerMode(Modality... modalities) {
        for (var modality : modalities) {
            modeMap.put(modality.getKey(), modality);
        }
    }

    public List<Modality> getAllModes(ModalityType modalityType) {
        return modeMap.values()
                .stream()
                .filter(modality -> modality.getModalityType() == modalityType)
                .sorted(Comparator.comparing(Modality::getName))
                .collect(Collectors.toList());
    }

    public boolean isActiveMode(String key) {
        return modeMap.values()
                .stream()
                .filter(Modality::isEnabled)
                .anyMatch(modality -> modality.getKey().equals(key));
    }

    public List<Modality> getModesActive(ModalityType modalityType) {
        return modeMap.values()
                .stream()
                .filter(modality -> modality.getModalityType() == modalityType)
                .filter(Modality::isEnabled)
                .sorted(Comparator.comparing(Modality::getName))
                .collect(Collectors.toList());
    }

    public HashMap<String, Modality> getModeMap() {
        return modeMap;
    }
}
