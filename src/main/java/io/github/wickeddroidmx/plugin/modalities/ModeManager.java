package io.github.wickeddroidmx.plugin.modalities;

import io.github.wickeddroidmx.plugin.modalities.modes.*;
import io.github.wickeddroidmx.plugin.modalities.scenarios.*;
import io.github.wickeddroidmx.plugin.modalities.settings.*;
import io.github.wickeddroidmx.plugin.modalities.teams.*;
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

    private SecondLifeMode secondLifeMode;

    private MolesMode molesMode;

    private EncyclopediaMode encyclopediaMode;

    private MoreOresMode moreOresMode;

    private ColdWeaponsMode coldWeaponsMode;


    private DamageCycleMode damageCycleMode;

    private SilentNightMode silentNightMode;

    private AnvilDiscountsMode anvilDiscountsMode;

    private FriendlyFireMode friendlyFireMode;
    private GraveRobbersMode graveRobbersMode;
    private HalfHeartMode halfHeartMode;
    private TradeParanoiaMode tradeParanoiaMode;
    private BestPVEMode bestPVEMode;
    private InventoryShuffleMode inventoryShuffleMode;

    // Team
    private LoveAtFirstSightMode loveAtFirstSightMode;
    private CaptainsMode captainsMode;
    private UnknownTeamsMode unknownTeamsMode;
    private BrothersMode brothersMode;
    private ChosenMode chosenMode;
    private WeakestLinkMode weakestLinkMode;
    private CrazyDayLightCycleMode crazyDayLightCycleMode;

    //Scenarios
    private GunsNRosesScenario gunsNRosesScenario;
    private ChickenFightScenario chickenFightScenario;
    private NoFallScenario noFallScenario;
    private SuperHeroesMode superHeroesMode;
    private HasteyBoysScenario hasteyBoysScenario;
    private TimberScenario timberScenario;
    private CutCleanScenario cutCleanScenario;
    private FireLessScenario fireLessScenario;
    private SiphonScenario siphonScenario;
    private TeamInventoryScenario teamInventoryScenario;
    private RegretFulWolfsScenario regretFulWolfsScenario;
    private DoggosMode doggosMode;
    private NuggetsScenario nuggetsScenario;
    private DoubleOresScenario doubleOresScenario;
    private CobwebLess cobwebLess;
    private ShieldLessScenario shieldLessScenario;
    private GoldLessScenario goldLessScenario;
    private LuckyLeavesScenario luckyLeavesScenario;
    private HeavyPocketsScenario heavyPocketsScenario;
    private StarterItemScenario starterItemScenario;
    private BattleParanoiaScenario battleParanoiaScenario;

    //UHC
    private UhcRunMode uhcRunMode;

    private UhcSkyHighMode uhcSkyHighMode;

    //SETTING
    private CobbleOnlySetting cobbleOnlySetting;
    private StarterBookSetting starterBookSetting;
    private SuspiciousStewLessSetting suspiciousStewLessSetting;
    private NoMobsSetting noMobsSetting;
    private NoBurnSetting noBurnSetting;
    private GoldenHeadSetting goldenHeadSetting;
    private SpawnerLastBreathSetting spawnerLastBreathSetting;

    private final HashMap<String, Modality> modeMap = new HashMap<>();

    public void registerModes() {
        registerMode(
                tanksMode,
                kingMode,
                noVillagesMode,
                noNetherMode,
                uhcRunMode,
                gunsNRosesScenario,
                timberScenario,
                graveRobbersMode,
                hasteyBoysScenario,
                cutCleanScenario,
                chickenFightScenario,
                halfHeartMode,
                loveAtFirstSightMode,
                fireLessScenario,
                noFallScenario,
                tradeParanoiaMode,
                siphonScenario,
                teamInventoryScenario,
                healthDarkStartMode,
                superHeroesMode,
                ultraAggressiveMode,
                jumperMode,
                backpackMode,
                noNametagMode,
                noAchievementsMode,
                noMobsSetting,
                advancedTacticsMode,
                regretFulWolfsScenario,
                nuggetsScenario,
                doggosMode,
                deathListMode,
                uhcSpainMode,
                cobwebLess,
                ultraDropsRandomMode,
                dropsRandomMode,
                starterBookSetting,
                secondLifeMode,
                molesMode,
                captainsMode,
                uhcSkyHighMode,
                anvilDiscountsMode,
                noBurnSetting,
                encyclopediaMode,
                coldWeaponsMode,
                shieldLessScenario,
                friendlyFireMode,
                goldenHeadSetting,
                damageCycleMode,
                silentNightMode,
                suspiciousStewLessSetting,
                doubleOresScenario,
                cobbleOnlySetting,
                spawnerLastBreathSetting,
                unknownTeamsMode,
                bestPVEMode,
                brothersMode,
                chosenMode,
                goldLessScenario,
                luckyLeavesScenario,
                weakestLinkMode,
                heavyPocketsScenario,
                starterItemScenario,
                inventoryShuffleMode,
                crazyDayLightCycleMode,
                battleParanoiaScenario
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
                .sorted(Comparator.comparing(Modality::getName))
                .collect(Collectors.toList());
    }

    public HashMap<String, Modality> getModeMap() {
        return modeMap;
    }
}
