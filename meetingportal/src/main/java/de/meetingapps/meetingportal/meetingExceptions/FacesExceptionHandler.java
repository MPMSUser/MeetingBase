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

import java.util.Iterator;
import java.util.Objects;

import jakarta.faces.FacesException;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;

/**
 * The Class FacesExceptionHandler.
 */
public class FacesExceptionHandler extends ExceptionHandlerWrapper {

    /**
     * Instantiates a new faces exception handler.
     *
     * @param wrapped the wrapped
     */
    public FacesExceptionHandler(final ExceptionHandler wrapped) {
        super(wrapped);
    }

    /**
     * Handle.
     *
     * @throws FacesException the faces exception
     */
    @Override
    public void handle() throws FacesException {
        
        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            final ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            final ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable throwable = context.getException();

            throwable = findCauseUsingPlainJava(throwable);

            final FacesContext fc = FacesContext.getCurrentInstance();

            try {
                System.out.println("### Faces Exception ###");

                final Flash flash = fc.getExternalContext().getFlash();
                flash.put("message", throwable.getMessage());
                flash.put("type", throwable.getClass().getSimpleName());
                flash.put("exception", throwable.getClass().getName());
                
                System.out.println("### message ### " + throwable.getMessage());
                System.out.println("### type ### " + throwable.getClass().getSimpleName());
                System.out.println("### exception ### " + throwable.getClass().getName());
//                for (int i=0;i<throwable.getStackTrace().length;i++){
//                    System.out.println(throwable.getStackTrace()[i]);
//                }

                final NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();

                navigationHandler.handleNavigation(fc, null, "/errorhandler.xhtml?faces-redirect=true");

                fc.renderResponse();
            } finally {
                iterator.remove();
            }
        }

        // Let the parent handle the rest
        getWrapped().handle();
    }

    /**
     * Helper method to find the exception root cause.
     *
     * @param throwable the throwable
     * @return the throwable
     */
    public static Throwable findCauseUsingPlainJava(final Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            System.out.println("cause: " + rootCause.getCause().getMessage());
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}