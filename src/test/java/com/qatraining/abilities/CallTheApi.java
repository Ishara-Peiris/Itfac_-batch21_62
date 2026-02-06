package com.qatraining.abilities;

import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class CallTheApi {
    public static Ability at(String baseUrl) {
        return CallAnApi.at(baseUrl);
    }
}
