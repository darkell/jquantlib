/*
JQuantLib is Copyright (c) 2007, Richard Gomes

All rights reserved.

This source code is release under the BSD License.

JQuantLib includes code taken from QuantLib.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.

    Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

    Neither the names of the copyright holders nor the names of the QuantLib
    Group and its contributors may be used to endorse or promote products
    derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package org.jquantlib.lang.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A specialized RuntimeException used internally by JQuantLib
 *
 * @author Richard Gomes
 */
public class LibraryException extends RuntimeException {
    private static Logger LOG = LoggerFactory.getLogger(LibraryException.class);

    public LibraryException() {
        super("LibraryException created");
        LOG.error("LibraryException created", this);
    }

    public LibraryException(String message) {
        super(message);
        LOG.error("LibraryException created", this);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
        LOG.error("LibraryException created", this);
    }

    public LibraryException(Throwable cause) {
        super(cause);
        LOG.error("LibraryException created", this);
    }

}