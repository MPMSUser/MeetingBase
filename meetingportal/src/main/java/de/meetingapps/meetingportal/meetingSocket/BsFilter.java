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
package de.meetingapps.meetingportal.meetingSocket;

import java.io.IOException;

import de.meetingapps.meetingportal.meetComAllg.CaTokenUtil;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/wortmeldungSocket/*")
public class BsFilter implements Filter{
    
    @Inject
    private CaTokenUtil caTokenUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String jwt = request.getParameter("jwt");
        if (jwt == null || jwt.trim().isEmpty()) {
            returnForbiddenError(response, "JWT Token ist nicht vorhanden");
            return;
        }
        if(caTokenUtil.validateToken(jwt)) {
            String loginKennung = caTokenUtil.getLoginkennung(jwt);
            String mandant = caTokenUtil.getMandant(jwt);
            String socketUser = mandant + "::" + loginKennung;
            request.setAttribute("socketUser", socketUser);
            chain.doFilter(new BsAuthenticatedRequest(request, socketUser), response);
        } else {
            returnForbiddenError(response, "JWT Token ist ungültig");
            return;
        }
        
        
    }
    
    private void returnForbiddenError(HttpServletResponse response, String message) 
            throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }

}
