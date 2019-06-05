package io.redgreen.benchpress.brokenrecord

import com.google.common.truth.Truth.assertThat
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

class BrokenRecordTest {
    private val cpuScheduler = TestScheduler()
    private val tenSecondsInMillis = Milliseconds.fromSeconds(10)
    private val justA = "A"

    @Test
    fun `it can fetch value as soon as there is a subscriber`() {
        // given
        val brokenRecord = BrokenRecord
            .createSource<String>(tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.triggerActions()

        // then
        with(testObserver) {
            assertValueCount(1)
            assertValue(justA)
        }
    }

    @Test
    fun `it should poll every 'x' seconds after the initial fetch (1st interval)`() {
        // given
        val brokenRecord = BrokenRecord
            .createSource<String>(tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.advanceTimeBy(19, SECONDS)

        // then
        with(testObserver) {
            assertValueCount(1 + 1)
            assertValues(justA, justA)
        }
    }

    @Test
    fun `it should poll every 'x' seconds after the initial fetch (5 intervals)`() {
        // given
        val brokenRecord = BrokenRecord
            .createSource<String>(tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.advanceTimeBy(50, SECONDS)

        // then
        with(testObserver) {
            assertValueCount(1 + 5)
            assertValues(justA, justA, justA, justA, justA, justA)
        }
    }

    @Test
    fun `it should not emit any value with initial delay`() {
        // given
        val initialDelay = Milliseconds.fromSeconds(5)
        val brokenRecord =
            BrokenRecord.createSource(initialDelay, tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.triggerActions()

        // then
        with(testObserver) {
            assertNoValues()
        }
    }

    @Test
    fun `it should emit a value when the initial delay elapses`() {
        // given
        val initialDelay = Milliseconds.fromSeconds(5)
        val brokenRecord = BrokenRecord
            .createSource(initialDelay, tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.advanceTimeBy(initialDelay.value, MILLISECONDS)

        // then
        with(testObserver) {
            assertValueCount(1)
            assertValues(justA)
        }
    }

    @Test
    fun `it should emit multiple values after the initial delay elapses`() {
        // given
        val initialDelay = Milliseconds.fromSeconds(5)
        val brokenRecord = BrokenRecord
            .createSource(initialDelay, tenSecondsInMillis, cpuScheduler) { Single.just(justA) }
        val testObserver = brokenRecord.poll().test()

        // when
        cpuScheduler.advanceTimeBy(40, SECONDS)

        // then
        with(testObserver) {
            assertValueCount(1 + 3)
            assertValues(justA, justA, justA, justA)
        }
    }

    @Test
    fun `it should poll based on value returned by the server response`() {

        // given
        val dummyData = DummyDataClass(justA,10)
        val serverResponse =
            BrokenRecord.createSource(tenSecondsInMillis, cpuScheduler) {
                Single.just(dummyData)
            }


        val testObserver = serverResponse.poll().test()
        //when
        cpuScheduler.triggerActions()

        val newInterval = testObserver.values().get(0).interval

        val pollingResponse =
            BrokenRecord.createSource(Milliseconds.fromSeconds(newInterval), cpuScheduler) {
                Single.just(justA)
            }

        with(testObserver){
            assertThat(this.values().get(0).interval).isEqualTo(dummyData.interval)
        }
    }
}