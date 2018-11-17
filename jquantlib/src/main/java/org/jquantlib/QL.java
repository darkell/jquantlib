/*
 Copyright (C) 2010 Richard Gomes

 This source code is release under the BSD License.

 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.

 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

/*
 Copyright (C) 2000, 2001, 2002, 2003 RiskMap srl
 Copyright (C) 2003, 2004, 2005, 2006, 2007 StatPro Italia srl

 This file is part of QuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://quantlib.org/

 QuantLib is free software: you can redistribute it and/or modify it
 under the terms of the QuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <quantlib-dev@lists.sf.net>. The license is also available online at
 <http://quantlib.org/license.shtml>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
*/

package org.jquantlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.jquantlib.lang.exceptions.LibraryException;

/**
 * Static methods for validation and emiting log messages
 *
 * @author Richard Gomes
 * @author Srinivas Hasti
 */
public class QL {

    /**
     * Throws an error if a <b>pre-condition</b> is not verified
     * <p>
     * @param condition is a condition to be verified
     * @param message is a message emitted.
     * @throws a LibraryException if the condition is not met
     */
    public static void require(
            final boolean condition,
            final String format,
            final Object...objects) throws RuntimeException {
        if (!condition)
            throw new LibraryException(String.format(format, objects));
    }

    /**
     * Throws an error if a <b>pre-condition</b> is not verified
     * <p>
     * @param condition is a condition to be verified
     * @param message is a message emitted.
     * @throws a LibraryException if the condition is not met
     */
    public static void require(
            final boolean condition,
            final String message) throws RuntimeException {
        if (!condition)
            throw new LibraryException(message);
    }

    /**
     * Throws an error if a <b>pre-condition</b> is not verified
     * <p>
     * @param condition is a condition to be verified
     * @param klass is a Class which extends RuntimeException
     * @param message is a message emitted.
     * @throws a LibraryException if the condition is not met
     */
    public static void require(
            final boolean condition,
            final Class<? extends RuntimeException> klass,
            final String message) throws RuntimeException {
        if (!condition) {
            try {
                final Constructor<? extends RuntimeException> c = klass.getConstructor(String.class);
                throw c.newInstance(message);
            } catch (final SecurityException e) {
                e.printStackTrace();
            } catch (final NoSuchMethodException e) {
                e.printStackTrace();
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            } catch (final InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void ensure(
            final boolean condition,
            final String format,
            final Object...objects) throws RuntimeException {
        if (!condition)
            throw new LibraryException(String.format(format, objects));
    }

    
    /**
     * Throws an error if a <b>post-condition</b> is not verified
     * <p>
     * @note  this method should <b>never</b> be removed from bytecode by AspectJ.
     *        If you do so, you must be plenty sure of effects and risks of this decision.
     * <p>
     * @param condition is a condition to be verified
     * @param message is a message emitted.
     * @throws a LibraryException if the condition is not met
     */
    public static void ensure(
            final boolean condition,
            final String message) throws RuntimeException {
        if (!condition)
            throw new LibraryException(message);
    }

    /**
     * This method to validate whether code is being run in
     * experimental mode or not
     */
    public static void validateExperimentalMode() {
        if (System.getProperty("EXPERIMENTAL") == null)
            throw new UnsupportedOperationException("Work in progress");
    }

}
