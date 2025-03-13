package org.rela.test_recorder.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.*

class RecordableExampleTest {
    val recordConfig = TestRecorderConfig(record = false)


    @Test
    fun `using object`() {
        val recorder = TestRecorder(record = false)

        recorder.recordingPath shouldBe "src/test/resources/recordings/org/rela/test_recorder/core/RecordableExampleTest/using object.json"

        val recordableExample = RecordableExample(recorder)
        val data = recordableExample.someMethod()
        data.name shouldBe "John"
    }

    @Test
    fun `using function`() = recordableTest(record = false) {
        it.recordingPath shouldBe "src/test/resources/recordings/org/rela/test_recorder/core/RecordableExampleTest/using function.json"

        val recordableExample = RecordableExample(it)
        val data = recordableExample.someMethod()
        data.name shouldBe "John"
    }

    @Test
    fun `using class level config`() {
        val recorder = recordConfig.startTest()

        recorder.recordingPath shouldBe "src/test/resources/recordings/org/rela/test_recorder/core/RecordableExampleTest/using class level config.json"

        val recordableExample = RecordableExample(recorder)
        val data = recordableExample.someMethod()
        data.name shouldBe "John"
    }

    @Test
    fun `using function with class level config`() = recordableTest(recordConfig) {
        it.recordingPath shouldBe "src/test/resources/recordings/org/rela/test_recorder/core/RecordableExampleTest/using function with class level config.json"

        val recordableExample = RecordableExample(it)
        val data = recordableExample.someMethod()
        data.name shouldBe "John"
    }


}
