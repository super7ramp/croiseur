<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Make a Release of Croiseur

### Audience

This how-to is aimed at developers with commit rights on the Croiseur repository desiring to make
a new release of the project.

### Steps

#### 1. Create a branch `release/${version}` with `$version` the version to release

`git checkout -b release/${version} master`

#### 2. Update version

- Update version in [`gradle.properties`](../../gradle.properties).
- Update version
  in [Croiseur GUI translations](../../croiseur-gui/croiseur-gui/src/main/resources/com/gitlab/super7ramp/croiseur/gui/WelcomeScreen.properties)
  (check all languages, not only English).
- Update SPI versions advertised in the documentation:
    - [How To: Plug a Dictionary Provider](../how-to/Plug-a-dictionary-provider.md)
    - [How To: Plug a Solver](../how-to/Plug-a-solver.md)
    - [How To: Plug a Presenter](../how-to/Plug-a-presenter.md)

#### 3. Update changelog

Update [`CHANGELOG.md`](../../CHANGELOG.md) with the version changes.

Tip: Use the [milestone page](https://gitlab.com/super7ramp/croiseur/-/milestones) to get the list
of all the tickets closed and MR merged for this release (provided they have been correctly
annotated).

#### 4. Create MR from `release/${version}` to `master`

Review that everything is OK, then merge.

#### 5. Create and push tag

- `git tag -m "${version description}" ${version}`
- `git push ${version}`

#### 6. Create release in Gitlab

Go to [Gitlab release page](https://gitlab.com/super7ramp/croiseur/-/releases) to make a new
release. Take example on the previous ones in case of doubt.

##### Artifacts

###### Portable zip distribution

The build pipeline on the `master` branch automatically creates a zip distribution of Croiseur CLI +
Croiseur GUI for Linux x86_64. Link to this artifact can be added in the release page.

###### Installable packages for Croiseur GUI

Installable packages of Croiseur GUI for Linux, Mac and Windows can be manually built
using [Conveyor](https://www.hydraulic.dev/), uploaded to the package registry, then linked in the
release page.

> This is experimental, not integrated with CI and native solver plugins are not included. Expect
> improvements in this area. See also #25.

Here are the manual steps:

1. [Download Conveyor](https://downloads.hydraulic.dev/conveyor/download.html) and add its binary to
   your `PATH`, if not already done.
2. Go to `croiseur-gui/croiseur-gui`.
3. Disable the solver plugins written in Rust, i.e. comment the following lines
   in [`build.gradle.kts`](../../croiseur-gui/croiseur-gui/build.gradle.kts):

```kts
runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
```

4. Build all the jars: `gradle jar`.
5. Run `conveyor make site`. This will generate packages
   under [`output`](../../croiseur-gui/croiseur-gui/output).
6. Upload the files
   to [the project package registry](https://gitlab.com/super7ramp/croiseur/-/packages/). Gitlab web
   interface does not provide a way to do it but this can be achieved with a script like:

```bash
PACKAGE_REGISTRY="https://gitlab.com/api/v4/projects/43029946/packages/generic/Croiseur-GUI"
HEADER="PRIVATE-TOKEN: $your_access_token" # fill this
VERSION=$the_new_version # fill this

for package in $(find output -regex ".*\(gz\|zip\|deb\|msix\)"); do
    package_name=$(basename $package)
    curl --header "$HEADER" \
         --upload-file $package "${PACKAGE_REGISTRY}/${VERSION}/${package_name}"
done
```

7. *Voilà*. You can now add a link to the uploaded packages on the release page. Mention that they
   were built by Conveyor.

#### 7. Update version with next version

[Update version](#2-update-version) to `${version + 1}-SNAPSHOT`.
No need to create an MR for this, if you have reached this point you can push directly on `master`. 
