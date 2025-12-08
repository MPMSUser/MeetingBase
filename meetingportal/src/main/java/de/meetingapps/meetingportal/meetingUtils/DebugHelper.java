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

/**
 * The Class DebugHelper.
 */
public class DebugHelper {

    /**
     * Get memory allocation.
     */
    public static String getMemoryAllocation() {
        var runtime = Runtime.getRuntime();
        var mega = 1024 * 1024;

        var maxMemory = runtime.maxMemory() / mega;
        var allocatedMemory = runtime.totalMemory() / mega;
        var freeMemory = runtime.freeMemory() / mega;
        var usedMemory = allocatedMemory - freeMemory;

        return String.format("Max memory: %d mb Allocated memory: %d mb Free memory: %d mb Used memory: %d mb",
                maxMemory, allocatedMemory, freeMemory, usedMemory);
    }
}
