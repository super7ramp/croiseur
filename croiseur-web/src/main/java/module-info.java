/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.web.presenter.WebPresenter;

/**
 * Web API, frontend to croiseur.
 */
module com.gitlab.super7ramp.croiseur.web {
    requires com.fasterxml.jackson.databind;
    requires com.gitlab.super7ramp.croiseur;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires java.logging;

    provides Presenter with WebPresenter;
}