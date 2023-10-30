package jp.co.yumemi.droidtraining

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltModules::class],
)
object FakeHiltModules
