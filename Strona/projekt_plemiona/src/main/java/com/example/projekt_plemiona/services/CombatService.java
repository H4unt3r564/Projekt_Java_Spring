package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Report;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageBuilding;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.repositories.VillageBuildingRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class CombatService {

    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;
    private final VillageBuildingRepository villageBuildingRepository;
    private final ReportRepository reportRepository;


    public CombatService(
            VillageRepository villageRepository,
            VillageUnitRepository villageUnitRepository,
            VillageBuildingRepository villageBuildingRepository,
            ReportRepository reportRepository
    ) {
        this.villageRepository = villageRepository;
        this.villageUnitRepository = villageUnitRepository;
        this.villageBuildingRepository = villageBuildingRepository;
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void attack(
            Long sourceVillageId,
            Long targetVillageId,
            List<Long> unitTypeId,
            List<Integer> amount
    ) {

        List<VillageUnits> attackVillage =
                villageUnitRepository.findByVillage_VillageId(sourceVillageId);

        List<VillageUnits> defVillage =
                villageUnitRepository.findByVillage_VillageId(targetVillageId);

        Village sourceVillage =
                villageRepository
                        .findById(sourceVillageId)
                        .orElseThrow();

        Village targetVillage =
                villageRepository
                        .findById(targetVillageId)
                        .orElseThrow();

        Long attackerPlayerId =
                sourceVillage
                        .getPlayer()
                        .getPlayerId();

        Long defenderPlayerId =
                targetVillage
                        .getPlayer()
                        .getPlayerId();

        long attackPower = 0;

        int rams = 0;

        for(int i = 0; i < unitTypeId.size(); i++) {

            if(unitTypeId.get(i) == 7L) {

                rams = amount.get(i);

                break;
            }
        }



        for (int i = 0; i < unitTypeId.size(); i++) {

            System.out.println(
                    "Unit: "
                            + unitTypeId.get(i)
                            + " Amount: "
                            + amount.get(i)
            );

            attackPower +=
                    (long) amount.get(i)
                            * getAttackPower(unitTypeId.get(i));
        }

        long defensePower = 0;

        VillageBuilding wall =
                villageBuildingRepository
                        .findByVillage_VillageIdAndBuildingType_TypeId(
                                targetVillageId,
                                7L
                        )
                        .orElse(null);

        int wallLevel = 0;

        if(wall != null) {
            wallLevel = wall.getLevelNumber();
        }

        int effectiveWallLevel =
                Math.max(
                        0,
                        wallLevel - (rams / 10)
                );

        for (VillageUnits unit : defVillage) {

            Long defenderTypeId =
                    (long) unit.getId().getUnitTypeId();

            defensePower +=
                    unit.getAmount()
                            * getDefensePower(defenderTypeId);
        }

        double wallBonus =
                1.0 + (effectiveWallLevel * 0.05);

        defensePower =
                Math.round(
                        defensePower * wallBonus
                );


        double lossRate =
                calculateLosses(
                        attackPower,
                        defensePower
                );

        if (attackPower > defensePower) {

            long appdef = applyDefenderLosses(
                    defVillage,
                    lossRate,
                    true
            );

            long appat = applyAttackerLosses(
                    attackVillage,
                    unitTypeId,
                    amount,
                    lossRate,
                    true
            );



            Report attackerReport = new Report();

            attackerReport.setPlayerId(attackerPlayerId);
            attackerReport.setType("BATTLE");
            attackerReport.setContentJson(
                    "RESULT=WIN;"
                            + "TARGET=" + targetVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + appat
                            + ";DEFENDER_LOSSES=" + appdef
            );

            Report defenderReport = new Report();

            defenderReport.setPlayerId(defenderPlayerId);
            defenderReport.setType("BATTLE");
            defenderReport.setContentJson(
                    "RESULT=LOSE;"
                            + "SOURCE=" + sourceVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + appat
                            + ";DEFENDER_LOSSES=" + appdef
            );

            reportRepository.save(attackerReport);
            reportRepository.save(defenderReport);





        } else {

            long appat = applyAttackerLosses(
                    attackVillage,
                    unitTypeId,
                    amount,
                    lossRate,
                    true
            );

            Report attackerReport = new Report();

            attackerReport.setPlayerId(attackerPlayerId);
            attackerReport.setType("BATTLE");
            attackerReport.setContentJson(
                    "RESULT=LOSE;"
                            + "TARGET=" + targetVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + appat
            );

            Report defenderReport = new Report();

            defenderReport.setPlayerId(defenderPlayerId);
            defenderReport.setType("BATTLE");
            defenderReport.setContentJson(
                    "RESULT=WIN;"
                            + "SOURCE=" + sourceVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + appat
            );

            reportRepository.save(attackerReport);
            reportRepository.save(defenderReport);
        }


    }


    private int getAttackPower(Long unitTypeId) {

        return switch (unitTypeId.intValue()) {

            case 1 -> 10;   // spear
            case 2 -> 15;   // sword
            case 3 -> 40;   // axe
            case 4 -> 25;   // archer
            case 5 -> 130;  // light cavalry
            case 6 -> 150;  // heavy cavalry
            case 7 -> 2;    // ram
            case 8 -> 100;  // catapult

            default -> 0;
        };
    }

    private int getDefensePower(Long unitTypeId) {

        return switch (unitTypeId.intValue()) {

            case 1 -> 15;
            case 2 -> 50;
            case 3 -> 10;
            case 4 -> 20;
            case 5 -> 30;
            case 6 -> 200;
            case 7 -> 30;
            case 8 -> 100;

            default -> 0;
        };
    }


    private double calculateLosses(
            long attackPower,
            long defensePower
    ) {

        if (attackPower <= 0) {
            return 0.0;
        }

        double ratio =
                (double) attackPower / defensePower;

        return Math.min(
                1.0,
                ratio / 2.0
        );
    }


    private long applyAttackerLosses(
            List<VillageUnits> attackVillage,
            List<Long> unitTypeIds,
            List<Integer> amounts,
            double lossRate,
            boolean attackerLost
    ) {
        long totalLosses = 0;
        for(int i = 0; i < unitTypeIds.size(); i++) {

            if(amounts.get(i) <= 0) {
                continue;
            }

            Long currentType =
                    unitTypeIds.get(i);

            VillageUnits unit =
                    attackVillage.stream()
                            .filter(v ->
                                    v.getUnitType()
                                            .getUnitTypeId()
                                            .equals(currentType))
                            .findFirst()
                            .orElse(null);

            if(unit == null) {
                continue;
            }

            long losses;

            if(attackerLost) {

                losses = amounts.get(i);

            } else {

                losses =
                        Math.round(
                                amounts.get(i)
                                        * lossRate
                        );
            }

            totalLosses += losses;

            unit.setAmount(
                    unit.getAmount()
                            - losses
            );


        }

        villageUnitRepository.saveAll(attackVillage);
        return totalLosses;
    }

    private long applyDefenderLosses(
            List<VillageUnits> defVillage,
            double lossRate,
            boolean defenderLost
    ) {

        long totalLosses = 0;

        for (VillageUnits unit : defVillage) {

            long losses;

            if (defenderLost) {

                losses = unit.getAmount();

            } else {

                losses =
                        Math.round(
                                unit.getAmount()
                                        * lossRate
                        );
            }

            totalLosses += losses;

            unit.setAmount(
                    unit.getAmount() - losses
            );
        }

        villageUnitRepository.saveAll(
                defVillage
        );

        return totalLosses;
    }

}
