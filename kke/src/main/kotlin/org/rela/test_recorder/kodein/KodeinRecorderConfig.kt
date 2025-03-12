//package org.rela.test_recorder.kodein
//
//import org.kodein.di.*
//import org.rela.test_recorder.core.*
//
//class KodeinRecorderConfig(
//    testObj: Any,
//    record: Boolean = false,
//): TestRecorderConfig(testObj, record) {
//
//    fun wrap(di: DI): DI {
//        TODO()
////        return DI.Module("TestRecorderModule", ) {
////            val testName = findTestName()
////            val testRecord = if (record) {
////                val newRecord = TestRecord()
////                saveTest(testName, newRecord)
////                newRecord
////            } else {
////                readRecording(testName)
////            }
////
//////            val testRecorder = TestRecorderImpl(testName, record, testRecord, this@KodeinRecorderConfig)
//////
//////            extend(di, copy = Copy.All)
//////            bind() from singleton { testRecorder.createHttpClientRecorder() }
//////            bind<TestRecorder>() with singleton {testRecorder}
//////            bind<ExposedDatabase>() with singleton {
//////                val overridden = overriddenInstance() as ExposedDatabase
//////                RecordingExposedDatabase(testRecorder, overridden)
//////            }
//////
////        }
//
//    }
////        return DI(allowSilentOverride = true) {
////                val testName = findTestName()
////                val testRecord = if (recording) {
////                    val newRecord = TestRecord()
////                    saveTest(testName, newRecord)
////                    newRecord
////                } else {
////                    readRecording(testName)
////                }
////
////                val testRecorder = TestRecorderImpl(testName, recording, testRecord, this@KodeinRecorderConfig)
////
////                extend(di, copy = Copy.All)
////                bind() from singleton { testRecorder.createHttpClientRecorder() }
////                bind<TestRecorder>() with singleton {testRecorder}
////                bind<ExposedDatabase>() with singleton {
////                    val overridden = overriddenInstance() as ExposedDatabase
////                    RecordingExposedDatabase(testRecorder, overridden)
////                }
////
////    }
//
//    open fun DI.Builder.testOverrides() {
//        bind<Foo> { singleton { Foo() } }
//    }
//
//
//
//}
//
//class Foo {}