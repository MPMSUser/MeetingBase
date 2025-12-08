/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.meetingapps.meetingportal.meetingUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class AtomicCounter.
 */
public class AtomicCounter {

    /** The value. */
    private AtomicInteger value;

    /**
     * Instantiates a new atomic counter.
     */
    public AtomicCounter() {
        super();
        this.value = new AtomicInteger();
    }

    /**
     * Instantiates a new atomic counter.
     *
     * @param value the initial value
     */
    public AtomicCounter(int value) {
        super();
        this.value = new AtomicInteger(value);
    }

    /**
     * Decrement.
     *
     * @return the int
     */
    public int decrement() {
        int oldValue = value.get();
        while (!value.compareAndSet(oldValue, oldValue - 1)) {
            oldValue = value.get();
        }
        return oldValue - 1;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return value.get();
    }

    /**
     * Increment.
     *
     * @return the int
     */
    public int increment() {
        int oldValue = value.get();
        while (!value.compareAndSet(oldValue, oldValue + 1)) {
            oldValue = value.get();
        }
        return oldValue + 1;
    }
}
