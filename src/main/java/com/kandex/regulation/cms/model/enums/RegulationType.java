package com.kandex.regulation.cms.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RegulationType {
    MAIN("main"), SUB("sub");
    private final String name;
}
