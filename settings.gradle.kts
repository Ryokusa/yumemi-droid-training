rootProject.name = "droidtraining"
include(":app")
include(":api")
include(":weatherapi")

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.10"
}

gitHooks {
    // Configuration
    createHooks() // actual hooks creation
    preCommit {
        tasks("ktlintFormat", "ktlintCheck") // tasks to run
    }
}
