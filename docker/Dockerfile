# SPDX-FileCopyrightText: 2025 Antoine Belvire
# SPDX-License-Identifier: GPL-3.0-or-later

FROM gradle:9.3.1-jdk25-alpine

# cargo: For Rust
# font-liberation: For JavaFx tests
# pango: For JavaFx tests
# reuse: For license checks
RUN apk --no-cache add --update cargo font-liberation pango reuse

# Generate Gradle API jar onc so that each build doesn't have to do it
WORKDIR /tmp/init-gradle
RUN touch build.gradle.kts && gradle --info --no-daemon tasks
WORKDIR /
RUN rm -rf /tmp/init-gradle
