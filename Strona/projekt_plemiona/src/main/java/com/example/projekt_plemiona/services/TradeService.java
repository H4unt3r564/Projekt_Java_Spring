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

            Integer offerWood,
            Integer offerClay,
            Integer offerIron,

            Integer offerSpear,
            Integer offerSword,
            Integer offerAxe,
            Integer offerArcher,
            Integer offerLight,
            Integer offerHeavy,
            Integer offerRam,
            Integer offerCatapult,

            Integer requestWood,
            Integer requestClay,
            Integer requestIron,

            Integer requestSpear,
            Integer requestSword,
            Integer requestAxe,
            Integer requestArcher,
            Integer requestLight,
            Integer requestHeavy,
            Integer requestRam,
            Integer requestCatapult

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
                offerWood,
                offerClay,
                offerIron

        );

        senderVillage.setWood(
                senderVillage.getWood() - offerWood
        );

        senderVillage.setClay(
                senderVillage.getClay() - offerClay
        );

        senderVillage.setIron(
                senderVillage.getIron() - offerIron
        );
        villageRepository.save(
                senderVillage
        );

        List<VillageUnits> units =
                villageUnitRepository
                        .findByVillage_VillageId(
                                senderVillage.getVillageId()
                        );

        removeUnits(units, 1L, offerSpear);
        removeUnits(units, 2L, offerSword);
        removeUnits(units, 3L, offerAxe);
        removeUnits(units, 4L, offerArcher);
        removeUnits(units, 5L, offerLight);
        removeUnits(units, 6L, offerHeavy);
        removeUnits(units, 7L, offerRam);
        removeUnits(units, 8L, offerCatapult);

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

                        + ";OFFER_WOOD=" + offerWood
                        + ";OFFER_CLAY=" + offerClay
                        + ";OFFER_IRON=" + offerIron

                        + ";OFFER_SPEAR=" + offerSpear
                        + ";OFFER_SWORD=" + offerSword
                        + ";OFFER_AXE=" + offerAxe
                        + ";OFFER_ARCHER=" + offerArcher

                        + ";OFFER_LIGHT=" + offerLight
                        + ";OFFER_HEAVY=" + offerHeavy

                        + ";OFFER_RAM=" + offerRam
                        + ";OFFER_CATAPULT=" + offerCatapult

                        + ";REQUEST_WOOD=" + requestWood
                        + ";REQUEST_CLAY=" + requestClay
                        + ";REQUEST_IRON=" + requestIron

                        + ";REQUEST_SPEAR=" + requestSpear
                        + ";REQUEST_SWORD=" + requestSword
                        + ";REQUEST_AXE=" + requestAxe
                        + ";REQUEST_ARCHER=" + requestArcher

                        + ";REQUEST_LIGHT=" + requestLight
                        + ";REQUEST_HEAVY=" + requestHeavy

                        + ";REQUEST_RAM=" + requestRam
                        + ";REQUEST_CATAPULT=" + requestCatapult
                        + ";STATUS=PENDING"
        );

        reportRepository.save(
                report
        );

        Report senderReport = new Report();

        senderReport.setPlayerId(
                sender.getPlayerId()
        );

        senderReport.setType(
                "TRADE_SENT"
        );

        senderReport.setContentJson(
                report.getContentJson()
        );

        reportRepository.save(
                senderReport
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



        Integer offerWood = getInt(content, "OFFER_WOOD");
        Integer offerClay = getInt(content, "OFFER_CLAY");
        Integer offerIron = getInt(content, "OFFER_IRON");

        Integer requestWood = getInt(content, "REQUEST_WOOD");
        Integer requestClay = getInt(content, "REQUEST_CLAY");
        Integer requestIron = getInt(content, "REQUEST_IRON");

        Integer offerSpear = getInt(content, "OFFER_SPEAR");
        Integer offerSword = getInt(content, "OFFER_SWORD");
        Integer offerAxe = getInt(content, "OFFER_AXE");
        Integer offerArcher = getInt(content, "OFFER_ARCHER");
        Integer offerLight = getInt(content, "OFFER_LIGHT");
        Integer offerHeavy = getInt(content, "OFFER_HEAVY");
        Integer offerRam = getInt(content, "OFFER_RAM");
        Integer offerCatapult = getInt(content, "OFFER_CATAPULT");

        Integer requestSpear = getInt(content, "REQUEST_SPEAR");
        Integer requestSword = getInt(content, "REQUEST_SWORD");
        Integer requestAxe = getInt(content, "REQUEST_AXE");
        Integer requestArcher = getInt(content, "REQUEST_ARCHER");
        Integer requestLight = getInt(content, "REQUEST_LIGHT");
        Integer requestHeavy = getInt(content, "REQUEST_HEAVY");
        Integer requestRam = getInt(content, "REQUEST_RAM");
        Integer requestCatapult = getInt(content, "REQUEST_CATAPULT");

        Long senderId =
                getLong(content, "SENDER");

        Player sender =
                playerRepository
                        .findById(senderId)
                        .orElseThrow();

        Village senderVillage =
                villageRepository
                        .findAllByPlayer_PlayerId(
                                sender.getPlayerId()
                        )
                        .stream()
                        .findFirst()
                        .orElseThrow();

        Village receiverVillage =
                villageRepository
                        .findAllByPlayer_PlayerId(
                                receiver.getPlayerId()
                        )
                        .stream()
                        .findFirst()
                        .orElseThrow();

        checkResources(
                receiverVillage,
                requestWood,
                requestClay,
                requestIron
        );

        List<VillageUnits> receiverUnits =
                villageUnitRepository.findByVillage_VillageId(
                        receiverVillage.getVillageId()
                );

        removeUnits(receiverUnits, 1L, requestSpear);
        removeUnits(receiverUnits, 2L, requestSword);
        removeUnits(receiverUnits, 3L, requestAxe);
        removeUnits(receiverUnits, 4L, requestArcher);
        removeUnits(receiverUnits, 5L, requestLight);
        removeUnits(receiverUnits, 6L, requestHeavy);
        removeUnits(receiverUnits, 7L, requestRam);
        removeUnits(receiverUnits, 8L, requestCatapult);

        senderVillage.setWood(
                senderVillage.getWood() + requestWood
        );

        senderVillage.setClay(
                senderVillage.getClay() + requestClay
        );

        senderVillage.setIron(
                senderVillage.getIron() + requestIron
        );

        receiverVillage.setWood(
                receiverVillage.getWood()
                        - requestWood
                        + offerWood
        );

        receiverVillage.setClay(
                receiverVillage.getClay()
                        - requestClay
                        + offerClay
        );

        receiverVillage.setIron(
                receiverVillage.getIron()
                        - requestIron
                        + offerIron
        );

        villageRepository.save(senderVillage);
        villageRepository.save(receiverVillage);

        List<VillageUnits> senderUnits =
                villageUnitRepository.findByVillage_VillageId(
                        senderVillage.getVillageId()
                );

        addUnits(senderUnits, 1L, requestSpear);
        addUnits(senderUnits, 2L, requestSword);
        addUnits(senderUnits, 3L, requestAxe);
        addUnits(senderUnits, 4L, requestArcher);
        addUnits(senderUnits, 5L, requestLight);
        addUnits(senderUnits, 6L, requestHeavy);
        addUnits(senderUnits, 7L, requestRam);
        addUnits(senderUnits, 8L, requestCatapult);

        addUnits(receiverUnits, 1L, offerSpear);
        addUnits(receiverUnits, 2L, offerSword);
        addUnits(receiverUnits, 3L, offerAxe);
        addUnits(receiverUnits, 4L, offerArcher);
        addUnits(receiverUnits, 5L, offerLight);
        addUnits(receiverUnits, 6L, offerHeavy);
        addUnits(receiverUnits, 7L, offerRam);
        addUnits(receiverUnits, 8L, offerCatapult);

        villageUnitRepository.saveAll(senderUnits);
        villageUnitRepository.saveAll(receiverUnits);

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

        Integer wood = getInt(content, "OFFER_WOOD");
        Integer clay = getInt(content, "OFFER_CLAY");
        Integer iron = getInt(content, "OFFER_IRON");

        Integer spear = getInt(content, "OFFER_SPEAR");
        Integer sword = getInt(content, "OFFER_SWORD");
        Integer axe = getInt(content, "OFFER_AXE");
        Integer archer = getInt(content, "OFFER_ARCHER");

        Integer light = getInt(content, "OFFER_LIGHT");
        Integer heavy = getInt(content, "OFFER_HEAVY");

        Integer ram = getInt(content, "OFFER_RAM");
        Integer catapult = getInt(content, "OFFER_CATAPULT");

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



        villageRepository.save(village);

        Village check =
                villageRepository
                        .findById(
                                village.getVillageId()
                        )
                        .orElseThrow();



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