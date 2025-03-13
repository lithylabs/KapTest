package org.rela.test_recorder.kodein

import org.kodein.di.*
import org.rela.test_recorder.*
import org.rela.test_recorder.core.*
import org.rela.test_recorder.exposed.*

object KodeinRecorder {
   fun wrap(di: DI, record: Boolean = false) = DI(allowSilentOverride = true) {

       // TestRecorder
       val config = TestRecorderConfig().apply {
           ignoreTestClasses.add(KodeinRecorder::class.qualifiedName!!)
       }
       val testRecorder = TestRecorder(config, record)
       bindSingleton<Recorder> { testRecorder }

       extend(di, copy = Copy.All)
       bind<TestRecorder>() with singleton {testRecorder}
       bindSingleton<ExposedDatabase>(overrides = true) {
           val existingDb: ExposedDatabase by di.instance<ExposedDatabase>()
           RecordingExposedDatabase(existingDb, testRecorder)
       }
   }
}