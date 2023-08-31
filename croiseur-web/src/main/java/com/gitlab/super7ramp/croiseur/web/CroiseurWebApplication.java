/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web;

import com.gitlab.super7ramp.croiseur.web.presenter.WebPresenter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * The Croiseur Web Application.
 */
@SpringBootApplication
public class CroiseurWebApplication {

    /**
     * Constructs an instance.
     *
     * @param applicationContext the Spring application context
     */
    public CroiseurWebApplication(final ApplicationContext applicationContext) {
        WebPresenter.inject(applicationContext);
    }
}
