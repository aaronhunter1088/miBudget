package com.miBudget.controllers;

import com.miBudget.main.MiBudgetState;

public abstract class MiBudgetController {

    public MiBudgetController() {
        MiBudgetState.initialize();
    }
}
