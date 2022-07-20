package com.miBudget.controllers;

import com.miBudget.core.MiBudgetState;

public abstract class MiBudgetController {

    public MiBudgetController() {
        MiBudgetState.initialize();
    }
}
