rootProject.name = "droidtraining"
include(":app")
include(":api")
include(":weatherapi")

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.10"
}

gitHooks {
    // Configuration
    preCommit {
        tasks("ktlintCheck")
    }
    createHooks(true) // actual hooks creation
}
