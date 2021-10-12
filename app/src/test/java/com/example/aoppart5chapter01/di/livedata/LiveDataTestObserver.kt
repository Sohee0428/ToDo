package com.example.aoppart5chapter01.di.livedata

import androidx.lifecycle.Observer

class LiveDataTestObserver<T> : Observer<T> {

    private val values: MutableList<T> = mutableListOf()

    override fun onChanged(t: T) {
        values.add(t)
    }

    fun assertValueSequence(sequence: List<T>): LiveDataTestObserver<T> {
        var i = 0
        val actualIterator = values.iterator()
        val expectedIterator = sequence.iterator()

        var actualNext: Boolean
        var expectedNext: Boolean

        while (true) {
            actualNext = actualIterator.hasNext()
            expectedNext = expectedIterator.hasNext()

            if (!actualNext || !expectedNext) break

            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (actual != expected) {
                throw AssertionError("actual: ${actual}, expected: ${expected}, index: $i")
            }

            i++
        }

        if (actualNext) {
            throw AssertionError("More values received then expected ($i)")
        }
        if (expectedNext) {
            throw AssertionError("Fewer values received then expected ($i)")
        }

        return this
    }
}