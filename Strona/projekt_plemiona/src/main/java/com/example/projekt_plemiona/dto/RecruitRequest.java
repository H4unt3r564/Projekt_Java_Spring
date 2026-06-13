package com.example.projekt_plemiona.dto;

public record RecruitRequest(
        Long villageId,
        Long unitTypeId,
        Integer amount
) {
}