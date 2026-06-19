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


import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        validateAttackRequest(
                sourceVillageId,
                targetVillageId,
                unitTypeId,
                amount
        );

        List<VillageUnits> attackVillage =
                villageUnitRepository.findByVillage_VillageId(sourceVillageId);

        validateAttackerUnits(
                attackVillage,
                unitTypeId,
                amount
        );

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

            String attackerLosses =
                    applyAttackerLosses(
                            attackVillage,
                            unitTypeId,
                            amount,
                            lossRate,
                            false
                    );

            String defenderLosses =
                    applyDefenderLosses(
                            defVillage,
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
                            + ";ATTACKER_LOSSES=" + attackerLosses
                            + ";DEFENDER_LOSSES=" + defenderLosses
            );

            Report defenderReport = new Report();

            defenderReport.setPlayerId(defenderPlayerId);
            defenderReport.setType("BATTLE");
            defenderReport.setContentJson(
                    "RESULT=LOSE;"
                            + "SOURCE=" + sourceVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + attackerLosses
                            + ";DEFENDER_LOSSES=" + defenderLosses
            );

            reportRepository.save(attackerReport);
            reportRepository.save(defenderReport);





        } else {

            String attackerLosses =
                    applyAttackerLosses(
                            attackVillage,
                            unitTypeId,
                            amount,
                            lossRate,
                            true
                    );

            String defenderLosses =
                    applyDefenderLosses(
                            defVillage,
                            lossRate,
                            false
                    );

            Report attackerReport = new Report();

            attackerReport.setPlayerId(attackerPlayerId);
            attackerReport.setType("BATTLE");
            attackerReport.setContentJson(
                    "RESULT=LOSE;"
                            + "TARGET=" + targetVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + attackerLosses
                            + ";DEFENDER_LOSSES=" + defenderLosses
            );

            Report defenderReport = new Report();

            defenderReport.setPlayerId(defenderPlayerId);
            defenderReport.setType("BATTLE");
            defenderReport.setContentJson(
                    "RESULT=WIN;"
                            + "SOURCE=" + sourceVillageId
                            + ";ATTACK=" + attackPower
                            + ";DEFENSE=" + defensePower
                            + ";ATTACKER_LOSSES=" + attackerLosses
                            + ";DEFENDER_LOSSES=" + defenderLosses
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

        if (attackPower <= 0 || defensePower <= 0) {
            return 0.0;
        }

        return (double)
                Math.min(attackPower, defensePower)
                / Math.max(attackPower, defensePower);
    }

    private String applyAttackerLosses(
            List<VillageUnits> attackVillage,
            List<Long> unitTypeIds,
            List<Integer> amounts,
            double lossRate,
            boolean attackerLost
    ) {

        StringBuilder result =
                new StringBuilder();

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

            unit.setAmount(
                    unit.getAmount()
                            - losses
            );

            if(losses > 0) {

                result.append(
                        unit.getUnitType()
                                .getCodeName()
                );

                result.append("=");

                result.append(losses);

                result.append(";");
            }
        }

        villageUnitRepository.saveAll(
                attackVillage
        );

        return result.toString();
    }



    private String applyDefenderLosses(
            List<VillageUnits> defVillage,
            double lossRate,
            boolean defenderLost
    ) {

        StringBuilder result =
                new StringBuilder();

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

            unit.setAmount(
                    unit.getAmount()
                            - losses
            );

            if (losses > 0) {

                result.append(
                        unit.getUnitType()
                                .getCodeName()
                );

                result.append("=");

                result.append(losses);

                result.append(";");
            }
        }

        villageUnitRepository.saveAll(
                defVillage
        );

        return result.toString();
    }


    private void validateAttackRequest(
            Long sourceVillageId,
            Long targetVillageId,
            List<Long> unitTypeIds,
            List<Integer> amounts
    ) {

        if (sourceVillageId == null
                || targetVillageId == null) {

            throw new IllegalArgumentException(
                    "Village id cannot be null"
            );
        }

        if (sourceVillageId.equals(targetVillageId)) {

            throw new IllegalArgumentException(
                    "Cannot attack own village"
            );
        }

        if (unitTypeIds == null
                || amounts == null) {

            throw new IllegalArgumentException(
                    "Units cannot be null"
            );
        }

        if (unitTypeIds.isEmpty()) {

            throw new IllegalArgumentException(
                    "No units selected"
            );
        }

        if (unitTypeIds.size() != amounts.size()) {

            throw new IllegalArgumentException(
                    "Invalid attack request"
            );
        }

        for (Integer amount : amounts) {

            if (amount == null
                    || amount <= 0) {

                throw new IllegalArgumentException(
                        "Invalid unit amount"
                );
            }
        }

        for (Long unitTypeId : unitTypeIds) {

            if (unitTypeId == null
                    || unitTypeId < 1
                    || unitTypeId > 8) {

                throw new IllegalArgumentException(
                        "Invalid unit type"
                );
            }
        }

        Set<Long> uniqueUnits = new HashSet<>();

        for (Long unitTypeId : unitTypeIds) {

            if (!uniqueUnits.add(unitTypeId)) {

                throw new IllegalArgumentException(
                        "Duplicate unit type"
                );
            }
        }
    }


    private void validateAttackerUnits(
            List<VillageUnits> attackVillage,
            List<Long> unitTypeIds,
            List<Integer> amounts
    ) {

        for (int i = 0; i < unitTypeIds.size(); i++) {

            Long currentType =
                    unitTypeIds.get(i);

            Integer requestedAmount =
                    amounts.get(i);

            VillageUnits unit =
                    attackVillage.stream()
                            .filter(v ->
                                    v.getUnitType()
                                            .getUnitTypeId()
                                            .equals(currentType))
                            .findFirst()
                            .orElse(null);

            if (unit == null) {

                throw new IllegalArgumentException(
                        "Village does not own unit type "
                                + currentType
                );
            }

            if (unit.getAmount()
                    < requestedAmount) {

                throw new IllegalArgumentException(
                        "Not enough units of type "
                                + currentType
                );
            }
        }
    }



}
