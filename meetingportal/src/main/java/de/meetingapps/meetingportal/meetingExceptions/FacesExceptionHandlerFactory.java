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
package de.meetingapps.meetingportal.meetingExceptions;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * A factory for creating FacesExceptionHandler objects.
 */
public class FacesExceptionHandlerFactory extends ExceptionHandlerFactory {

    /**
     * Instantiates a new faces exception handler factory.
     *
     * @param wrapped the wrapped
     */
    public FacesExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        super(wrapped);
    }

    /**
     * Gets the exception handler.
     *
     * @return the exception handler
     */
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler parentHandler = getWrapped().getExceptionHandler();
        return new FacesExceptionHandler(parentHandler);
    }

}
