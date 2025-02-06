package org.rela.test_recorder.core

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.*
import org.junit.jupiter.api.*
import org.rela.test_recorder.TestRecorder

class RecordableTest {
    @Test
    fun `test recordable record and playback`() {
        val recorder = MockTestRecorder()
        val recordableExample = RecordableExample(recorder)

        recorder.readyTest(true)
        val data = recordableExample.someMethod()
        recorder.recordCalled.shouldBeTrue()

        recorder.readyTest(false)
        val data2 = recordableExample.someMethod()
        recorder.playbackCalled.shouldBeTrue()
        data2.name shouldBe data.name
        data2.age shouldBe data.age
    }

    @Test
    fun `test recordableNullable record and playback with value`() {
        val recorder = MockTestRecorder()
        val recordableExample = RecordableExample(recorder)

        recorder.readyTest(true)
        val data = recordableExample.someMethodNullable()
        recorder.recordCalled.shouldBeTrue()

        recorder.readyTest(false)
        val data2 = recordableExample.someMethodNullable()
        recorder.playbackCalled.shouldBeTrue()
        data2.shouldNotBeNull()
        data2.name shouldBe data?.name
        data2.age shouldBe data?.age
    }

    @Test
    fun `test recordableNullable record and playback with null`() {
        val recorder = MockTestRecorder()
        val recordableExample = RecordableExample(recorder)

        recorder.readyTest(true)
        val data = recordableExample.someMethodNull()
        recorder.recordCalled.shouldBeTrue()

        recorder.readyTest(false)
        val data2 = recordableExample.someMethodNull()
        recorder.playbackCalled.shouldBeTrue()
        data2.shouldBeNull()
    }


}

class RecordableExample (
    override val recorder: TestRecorder? = null
): Recordable {
    fun someMethod() = recordable {
        ExampleData("John", 25)
    }

    fun someMethodNullable() = recordableNullable {
        ExampleData("John", 25)
    }

    fun someMethodNull() = recordableNullable {
        val data: ExampleData? = null
        data
    }
}

@Serializable
class ExampleData(
    val name: String,
    val age: Int
)

class MockTestRecorder: TestRecorder {
    override var record: Boolean = true
    var json: String? = null
    var recordCalled = false
    var playbackCalled = false

    override fun recordEvent(callName: String, json: String?) {
        this.json = json
        recordCalled = true
    }

    override fun fetchPlaybackJson(callName: String): String? {
        playbackCalled = true
        return json
    }

    fun readyTest(record: Boolean) {
        this.record = record
        recordCalled = false
        playbackCalled = false
    }
}
