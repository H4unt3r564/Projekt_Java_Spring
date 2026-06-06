package com.example.projekt_plemiona.services;

import com.example.projekt_plemiona.models.Player;
import com.example.projekt_plemiona.models.Report;
import com.example.projekt_plemiona.models.Village;
import com.example.projekt_plemiona.models.VillageUnits;
import com.example.projekt_plemiona.repositories.PlayerRepository;
import com.example.projekt_plemiona.repositories.ReportRepository;
import com.example.projekt_plemiona.repositories.VillageRepository;
import com.example.projekt_plemiona.repositories.VillageUnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private final ReportRepository reportRepository;
    private final VillageRepository villageRepository;
    private final VillageUnitRepository villageUnitRepository;
    private final PlayerRepository playerRepository;

    public TradeService(
            ReportRepository reportRepository,
            VillageRepository villageRepository,
            VillageUnitRepository villageUnitRepository,
            PlayerRepository playerRepository
    ) {
        this.reportRepository = reportRepository;
        this.villageRepository = villageRepository;
        this.villageUnitRepository = villageUnitRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void createOffer(

            Player sender,
            Long receiverId,

            Integer wood,
            Integer clay,
            Integer iron,

            Integer spear,
            Integer sword,
            Integer axe,
            Integer archer,
            Integer light,
            Integer heavy,
            Integer ram,
            Integer catapult

    ) {

        Village senderVillage =
                villageRepository
                        .findAllByPlayer_PlayerId(
                                sender.getPlayerId()
                        )
                        .stream()
                        .findFirst()
                        .orElseThrow();

        checkResources(
                senderVillage,
                wood,
                clay,
                iron
        );

        senderVillage.setWood(
                senderVillage.getWood() - wood
        );

        senderVillage.setClay(
                senderVillage.getClay() - clay
        );

        senderVillage.setIron(
                senderVillage.getIron() - iron
        );

        villageRepository.save(
                senderVillage
        );

        List<VillageUnits> units =
                villageUnitRepository
                        .findByVillage_VillageId(
                                senderVillage.getVillageId()
                        );

        removeUnits(units, 1L, spear);
        removeUnits(units, 2L, sword);
        removeUnits(units, 3L, axe);
        removeUnits(units, 4L, archer);
        removeUnits(units, 5L, light);
        removeUnits(units, 6L, heavy);
        removeUnits(units, 7L, ram);
        removeUnits(units, 8L, catapult);

        villageUnitRepository.saveAll(
                units
        );

        Report report =
                new Report();

        report.setPlayerId(
                receiverId
        );

        report.setType(
                "TRADE_OFFER"
        );

        report.setContentJson(

                "SENDER=" + sender.getPlayerId()

                        + ";VILLAGE=" + senderVillage.getVillageId()

                        + ";WOOD=" + wood
                        + ";CLAY=" + clay
                        + ";IRON=" + iron

                        + ";SPEAR=" + spear
                        + ";SWORD=" + sword
                        + ";AXE=" + axe
                        + ";ARCHER=" + archer

                        + ";LIGHT=" + light
                        + ";HEAVY=" + heavy

                        + ";RAM=" + ram
                        + ";CATAPULT=" + catapult

                        + ";STATUS=PENDING"
        );

        reportRepository.save(
                report
        );
    }

    private void checkResources(
            Village village,
            Integer wood,
            Integer clay,
            Integer iron
    ) {

        if (village.getWood() < wood) {
            throw new RuntimeException(
                    "Not enough wood"
            );
        }

        if (village.getClay() < clay) {
            throw new RuntimeException(
                    "Not enough clay"
            );
        }

        if (village.getIron() < iron) {
            throw new RuntimeException(
                    "Not enough iron"
            );
        }
    }

    private void removeUnits(
            List<VillageUnits> units,
            Long unitTypeId,
            int amount
    ) {

        if (amount <= 0) {
            return;
        }

        VillageUnits unit =
                units.stream()
                        .filter(u ->
                                u.getUnitType()
                                        .getUnitTypeId()
                                        .equals(unitTypeId))
                        .findFirst()
                        .orElseThrow();

        if (unit.getAmount() < amount) {

            throw new RuntimeException(
                    "Not enough units"
            );
        }

        unit.setAmount(
                unit.getAmount() - amount
        );
    }

    @Transactional
    public void acceptOffer(
            Long reportId,
            Player receiver
    ) {

        Report report =
                reportRepository
                        .findById(reportId)
                        .orElseThrow();

        String content =
                report.getContentJson();



        Integer wood = getInt(content, "WOOD");

        System.out.println(
                "RECEIVER PLAYER = "
                        + receiver.getPlayerId()
        );

        System.out.println(
                "REPORT CONTENT = "
                        + content
        );

        System.out.println(
                "WOOD FROM REPORT = "
                        + wood
        );
        Integer clay = getInt(content, "CLAY");
        Integer iron = getInt(content, "IRON");

        Integer spear = getInt(content, "SPEAR");
        Integer sword = getInt(content, "SWORD");
        Integer axe = getInt(content, "AXE");
        Integer archer = getInt(content, "ARCHER");

        Integer light = getInt(content, "LIGHT");
        Integer heavy = getInt(content, "HEAVY");

        Integer ram = getInt(content, "RAM");
        Integer catapult = getInt(content, "CATAPULT");

        Village village =
                villageRepository
                        .findAllByPlayer_PlayerId(
                                receiver.getPlayerId()
                        )
                        .stream()
                        .findFirst()
                        .orElseThrow();

        System.out.println(
                "VILLAGE ID = "
                        + village.getVillageId()
        );

        System.out.println(
                "WOOD BEFORE = "
                        + village.getWood()
        );

        village.setWood(
                village.getWood() + wood
        );

        System.out.println(
                "WOOD AFTER = "
                        + village.getWood()
        );

        village.setClay(
                village.getClay() + clay
        );

        village.setIron(
                village.getIron() + iron
        );

        villageRepository.save(village);

        List<VillageUnits> units =
                villageUnitRepository
                        .findByVillage_VillageId(
                                village.getVillageId()
                        );

        addUnits(units, 1L, spear);
        addUnits(units, 2L, sword);
        addUnits(units, 3L, axe);
        addUnits(units, 4L, archer);
        addUnits(units, 5L, light);
        addUnits(units, 6L, heavy);
        addUnits(units, 7L, ram);
        addUnits(units, 8L, catapult);

        villageUnitRepository.saveAll(units);

        report.setContentJson(
                content.replace(
                        "STATUS=PENDING",
                        "STATUS=ACCEPTED"
                )
        );

        reportRepository.save(report);
    }

    @Transactional
    public void rejectOffer(
            Long reportId
    ) {

        Report report =
                reportRepository
                        .findById(reportId)
                        .orElseThrow();

        String content =
                report.getContentJson();

        Long villageId =
                getLong(content, "VILLAGE");

        Integer wood = getInt(content, "WOOD");
        Integer clay = getInt(content, "CLAY");
        Integer iron = getInt(content, "IRON");

        Integer spear = getInt(content, "SPEAR");
        Integer sword = getInt(content, "SWORD");
        Integer axe = getInt(content, "AXE");
        Integer archer = getInt(content, "ARCHER");

        Integer light = getInt(content, "LIGHT");
        Integer heavy = getInt(content, "HEAVY");

        Integer ram = getInt(content, "RAM");
        Integer catapult = getInt(content, "CATAPULT");

        Village village =
                villageRepository
                        .findById(villageId)
                        .orElseThrow();

        village.setWood(
                village.getWood() + wood
        );

        village.setClay(
                village.getClay() + clay
        );



        village.setIron(
                village.getIron() + iron
        );

        System.out.println(
                "SAVING VILLAGE = "
                        + village.getVillageId()
        );

        villageRepository.save(village);

        Village check =
                villageRepository
                        .findById(
                                village.getVillageId()
                        )
                        .orElseThrow();

        System.out.println(
                "WOOD AFTER SAVE = "
                        + check.getWood()
        );

        List<VillageUnits> units =
                villageUnitRepository
                        .findByVillage_VillageId(
                                villageId
                        );

        addUnits(units, 1L, spear);
        addUnits(units, 2L, sword);
        addUnits(units, 3L, axe);
        addUnits(units, 4L, archer);
        addUnits(units, 5L, light);
        addUnits(units, 6L, heavy);
        addUnits(units, 7L, ram);
        addUnits(units, 8L, catapult);

        villageUnitRepository.saveAll(units);

        report.setContentJson(
                content.replace(
                        "STATUS=PENDING",
                        "STATUS=REJECTED"
                )
        );

        reportRepository.save(report);
    }

    private void addUnits(
            List<VillageUnits> units,
            Long unitTypeId,
            int amount
    ) {

        if(amount <= 0) {
            return;
        }

        VillageUnits unit =
                units.stream()
                        .filter(v ->
                                v.getUnitType()
                                        .getUnitTypeId()
                                        .equals(unitTypeId))
                        .findFirst()
                        .orElseThrow();

        unit.setAmount(
                unit.getAmount() + amount
        );
    }

    private Integer getInt(
            String content,
            String key
    ) {

        for(String part : content.split(";")) {

            if(part.startsWith(key + "=")) {

                return Integer.parseInt(
                        part.substring(
                                key.length() + 1
                        )
                );
            }
        }

        return 0;
    }

    private Long getLong(
            String content,
            String key
    ) {

        for(String part : content.split(";")) {

            if(part.startsWith(key + "=")) {

                return Long.parseLong(
                        part.substring(
                                key.length() + 1
                        )
                );
            }
        }

        return 0L;
    }

}