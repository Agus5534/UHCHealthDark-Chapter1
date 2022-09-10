package io.github.wickeddroidmx.plugin.modalities;

import io.github.wickeddroidmx.plugin.modalities.modes.*;
import io.github.wickeddroidmx.plugin.modalities.scenarios.*;
import io.github.wickeddroidmx.plugin.modalities.teams.LoveAtFirstSightMode;
import io.github.wickeddroidmx.plugin.modalities.uhc.UhcRunMode;
import me.yushust.inject.InjectAll;

import javax.inject.Singleton;
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
    private HealthDarkStartMode healthDarkStartMode;
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

    // Team
    private LoveAtFirstSightMode loveAtFirstSightMode;

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
    private TradeParanoia tradeParanoia;
    private CobwebLess cobwebLess;

    //UHC
    private UhcRunMode uhcRunMode;

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
                healthDarkStartMode,
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
                dropsRandomMode
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
                .collect(Collectors.toList());
    }

    public HashMap<String, Modality> getModeMap() {
        return modeMap;
    }
}
